import React, {useContext, useState} from 'react';
import {formatTimeAgo, getImageMime} from "@/utils/format.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faImage, faXmark} from "@fortawesome/free-solid-svg-icons";
import {createPostComment, createReplyComment} from "@/APIs/comment.js";
import {updateCommentRecursive} from "@/components/CommentContainer.jsx";
import AuthContext from "@/context/AuthContext.jsx";

const SendCommentInput = ({postId, setPosts, setPostComments, comment, setShowResponse}) => {
    const {user} = useContext(AuthContext);
    const [textInput, setTextInput] = useState(comment ? "@" + comment.userSummary.username : "");
    const [commentMedia, setCommentMedia] = useState(null);

    const getFormData = () => {
        const formData = new FormData();
        formData.append("content", textInput);
        formData.append("file", commentMedia);
        setPosts(prev => prev.map(tmp => {
            if(tmp.id === postId) {
                return {
                    ...tmp,
                    totalComment: tmp.totalComment + 1,
                }
            }
            return tmp;
        }))
        return formData;
    }

    const handleSubmitComment = async () => {
        try {
            if(textInput === "" && !commentMedia) return;
            if(!comment){
                const formData = getFormData();
                const response = await createPostComment(postId, formData);
                response.commentedAt = formatTimeAgo(response.commentedAt);
                setPostComments(prev => ([response, ...prev]));
                setTextInput("");
                setCommentMedia(null);
            }else{
                const formData = getFormData();
                const response = await createReplyComment(comment.id, formData);
                response.commentedAt = formatTimeAgo(response.commentedAt);
                setPostComments(prev => updateCommentRecursive(prev, comment.id, [response]));
                setTextInput("");
                setCommentMedia(null);
                setShowResponse(true);
            }
        }catch (e){
            console.log(e);
        }
    }

    return (
        <div className="send-comment-bounding">
            <img className="comment-sender-avatar" src={`data:${getImageMime(user.avatar)};base64,${user.avatar}`} alt=""/>
            <div style={{
                padding: '10px',
                backgroundColor: '#e6e6e6',
                borderRadius: '15px',
                width: '100%',
            }}>
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '5px'
                }}>
                    <textarea
                        value={textInput}
                        placeholder="Viết bình luận"
                        onChange={(e) => setTextInput(e.target.value)}
                        className="comment-input"
                        onInput={(e) => {
                            e.target.style.height = 'auto';
                            e.target.style.height = e.target.scrollHeight + 'px';
                        }}
                        onKeyDown={(e) => {
                            if(e.key === "Enter" && !e.shiftKey){
                                e.preventDefault();
                                handleSubmitComment();
                            }
                        }}
                    />
                    {commentMedia &&
                        <div style={{
                            position: 'relative',
                            alignSelf: 'flex-start'
                        }}>
                            <img className="comment-media" src={URL.createObjectURL(commentMedia)} alt=""/>
                            <FontAwesomeIcon icon={faXmark} className="icon-gray-background"
                                 style={{
                                     position: 'absolute',
                                     top: '5px',
                                     right: '5px',
                                     cursor: 'pointer',
                                 }}
                                 onClick={() => setCommentMedia(null)}
                            />
                        </div>
                    }
                </div>
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                }}>
                    <label htmlFor="comment-image-upload">
                        <FontAwesomeIcon icon={faImage} fontSize="25px" color="#E53935" cursor="pointer"/>
                        <input
                            id="comment-image-upload"
                            type="file"
                            hidden
                            accept="image/*, video/*"
                            onChange={(e) => setCommentMedia(e.target.files[0])}
                        />
                    </label>
                    <div className="send-icon" style={{
                        width: '27px',
                        height: '27px'
                    }} onClick={handleSubmitComment}></div>
                </div>
            </div>
        </div>
    );
};

export default SendCommentInput;