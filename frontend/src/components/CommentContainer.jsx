import React, {useState} from 'react';
import {getImageMime} from "@/utils/format.js";
import SendCommentInput from "@/components/SendCommentInput.jsx";
import {getResponse} from "../APIs/comment.js";

export const updateCommentRecursive = (commentList, commentId, responses) => {
    return commentList.map(comment => {
        if (comment.id === commentId) {
            return {
                ...comment,
                response: [...(comment.response || []), ...responses]
            };
        }
        if (comment.response && comment.response.length > 0) {
            return {
                ...comment,
                response: updateCommentRecursive(comment.response, commentId, responses)
            };
        }
        return comment;
    });
};

const findCommentById = (comments, id) => {
    for (const comment of comments) {
        if (comment.id === id) return comment;
        if (comment.response) {
            const found = findCommentById(comment.response, id);
            if (found) return found;
        }
    }
    return null;
};

const CommentContainer = ({commentId, postComments, setPostComments, setPosts}) => {
    const [createResponse, setCreateResponse] = useState(false);
    const [showResponse, setShowResponse] = useState(false);

    const comment = findCommentById(postComments, commentId);
    if (!comment) return null;

    const handleClickShowResponse = async () => {
        try {
            const responses = await getResponse(comment.id);
            setPostComments(prev => updateCommentRecursive(prev, comment.id, responses));
            setShowResponse(true);
        } catch (e) {
            console.log(e);
        }
    };

    return (
        <React.Fragment>
            <img className="comment-sender-avatar" src={`data:${getImageMime(comment.userSummary.avatar)};base64,${comment.userSummary.avatar}`} alt=""/>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '5px', width: '100%' }}>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
                    <div style={{ padding: '10px', backgroundColor: '#e6e6e6', borderRadius: '15px', alignSelf: 'flex-start' }}>
                        <p className="comment-sender-name">{comment.userSummary.username}</p>
                        {comment.content !== "" && <p className="comment-sender-content">{comment.content}</p>}
                    </div>
                    {comment.mediaUrl && (
                        <img className="comment-media" src={comment.mediaUrl} alt=""/>
                    )}
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', marginLeft: '10px', gap: '5px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-around', fontSize: '12px', gap: '15px', alignSelf: 'flex-start', fontWeight: 'bold', color: '#a3a3a3' }}>
                        <p>{comment.commentedAt}</p>
                        <p>Thích</p>
                        <p style={{ cursor: 'pointer' }} onClick={() => setCreateResponse(true)}>Trả lời</p>
                    </div>

                    {comment.haveResponses && !showResponse && (
                        <p
                            style={{ fontWeight: 'bold', color: '#a3a3a3', alignSelf: 'flex-start', fontSize: '12px', cursor: 'pointer', marginTop: '5px' }}
                            onClick={handleClickShowResponse}
                        >
                            Hiển thị tất cả phản hồi
                        </p>
                    )}

                    {showResponse && comment.response && comment.response.map((data) => (
                        <div key={data.id} className="response-container">
                            <CommentContainer commentId={data.id} postComments={postComments} setPostComments={setPostComments} setPosts={setPosts}/>
                        </div>
                    ))}

                    <div style={{ width: '100%', maxWidth: '600px' }}>
                        {createResponse && (
                            <SendCommentInput
                                setPostComments={setPostComments}
                                setPosts={setPosts}
                                comment={comment}
                            />
                        )}
                    </div>
                </div>
            </div>
        </React.Fragment>
    );
};

export default CommentContainer;
