import React, {useContext, useState} from 'react';
import '../styles/Post.css';
import CurtainContext from "@/context/CurtainContext.jsx";
import PostContainer from "@/components/PostContainer.jsx";
import {getPostComment} from "@/APIs/comment.js";
import {faXmark} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import '../styles/Comment.css'
import AuthContext from "@/context/AuthContext.jsx";
import CreatePostPopup from "@/components/CreatePostPopup.jsx";
import SendCommentInput from "@/components/SendCommentInput.jsx";
import CommentContainer from "@/components/CommentContainer.jsx";

const PostsContainer = ({posts, setPosts, ref}) => {
    const { showCurtain, setShowCurtain } = useContext(CurtainContext);
    const [currentPost, setCurrentPost] = useState(null);
    const [postComments, setPostComments] = useState([]);
    const {user} = useContext(AuthContext);

    const reactions = [
        {name: "LIKE", value: "/public/reactions/like.png"},
        {name: "LOVE", value: "/public/reactions/heart.png"},
        {name: "HAHA", value: "/public/reactions/haha.png"},
        {name: "CRY", value: "/public/reactions/sad.png"},
        {name: "WOW", value: "/public/reactions/wow.png"},
        {name: "ANGRY", value: "/public/reactions/angry.png"},
    ]

    const handleClickPost = async (post) => {
        setCurrentPost(post);
        setPostComments(await getPostComment(post.id));
        setShowCurtain(true);
    }

    const handleClosePopup = () => {
        setCurrentPost(null);
        setShowCurtain(false);
    }

    return posts.length > 0 && (
        <React.Fragment>
            {showCurtain &&
                <div className="popup" style={{
                    width: '730px',
                    gap: '0px',
                }}>
                    {currentPost ? <React.Fragment>
                        <h3 className="popup-title" style={{margin: 0}}>{`Bài viết của ${currentPost.userSummary.username}`}</h3>
                        <div style={{
                            maxHeight: '600px',
                            overflowY: 'auto',
                        }}>
                            <PostContainer post={currentPost} reactions={reactions} setPosts={setPosts} index={0} setCurrentPost={setCurrentPost}/>
                            <div className="comment-bounding">
                                {postComments.length > 0 && postComments.map((comment, index) => (
                                    <div className="comment" key={index}>
                                        <CommentContainer commentId={comment.id} postComments={postComments} setPostComments={setPostComments} setPosts={setPosts}/>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <SendCommentInput setPosts={setPosts} postId={currentPost.id} setPostComments={setPostComments}/>
                        <FontAwesomeIcon icon={faXmark} fontSize="20px" className="popup-close-icon" onClick={handleClosePopup}/>
                    </React.Fragment> : <CreatePostPopup setPosts={setPosts} setShowCurtain={setShowCurtain} createPost={false} opponent={user} setCreatePost={setShowCurtain}/>
                    }
                </div>
            }
            <div className="posts-container">
                {posts.map((post, index) => (
                    <PostContainer key={index} post={post} reactions={reactions} setPosts={setPosts} handleClickPost={handleClickPost} setCurrentPost={setCurrentPost}/>
                ))}
                <div ref={ref}></div>
            </div>
        </React.Fragment>
    );
};

export default PostsContainer;