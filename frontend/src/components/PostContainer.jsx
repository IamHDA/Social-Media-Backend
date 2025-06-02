import React, {useContext} from 'react';
import {getImageMime} from "@/utils/format.js";
import {Link, useNavigate} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEarthAsia, faUserGroup, faXmark} from "@fortawesome/free-solid-svg-icons";
import LikeButton from "@/components/LikeButton.jsx";
import CurtainContext from "@/context/CurtainContext.jsx";

const PostContainer = ({ post, reactions, setPosts, handleClickPost, setCurrentPost }) => {
    const { showCurtain, setShowCurtain, setCreatePost } = useContext(CurtainContext);
    const navigate = useNavigate();

    const getPostHeader = (tmpPost) => {
        return (
            <div className="post-header">
                <img className="post-creator-avatar" src={`data:${getImageMime(tmpPost.userSummary.avatar)};base64,${tmpPost.userSummary.avatar}`} alt="" onClick={
                    () => navigate(`/profile/${tmpPost.userSummary.id}`)
                }/>
                <div style={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    flexDirection: 'column',
                    gap: '3px',
                }}>
                    <Link to={`/profile/${tmpPost.userSummary.id}`} className="post-creator-name">{tmpPost.userSummary.username}</Link>
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '5px',
                        fontSize: '15px',
                        color: 'rgb(179,179,179)',
                        fontWeight: 'bold'
                    }}>
                        <p>{tmpPost.updatedAt ? tmpPost.updatedAt : tmpPost.createdAt}</p>
                        <span className="spacing-dot"></span>
                        {tmpPost.privacy === "PUBLIC" ? <FontAwesomeIcon icon={faEarthAsia} /> : <FontAwesomeIcon icon={faUserGroup} />}
                    </div>
                </div>
            </div>
        );
    }

    const getPostBody = () => {
        return (
            <div className="post-body" style={{
                backgroundImage: post.backgroundUrl && `url(${post.backgroundUrl})`,
                justifyContent: post.backgroundUrl && 'center',
                alignItems: post.backgroundUrl && 'center',
                minHeight: post.backgroundUrl && '300px',
            }}>
                {!post.parentPost ? (
                    <React.Fragment>
                        <p className="post-content" style={{
                            padding: post.backgroundUrl && '40px',
                            fontWeight: post.backgroundUrl && 'bold',
                            fontSize: post.backgroundUrl && '28px',
                            color: post.backgroundUrl && 'white',
                            textAlign: post.backgroundUrl && 'center'
                        }}>{post.content}</p>
                        {!post.backgroundUrl &&
                            <div className={`post-media-grid post-media-count-${post.mediaList.length} ${post.mediaList.length}`}>
                                {post.mediaList.map((media, index) => (
                                    <img key={index} src={media.url} alt=""/>
                                ))}
                            </div>
                        }
                    </React.Fragment>
                ) : (
                    <React.Fragment>
                        <p className="post-content" style={{
                            padding: post.backgroundUrl && '40px',
                            fontWeight: post.backgroundUrl && 'bold',
                            fontSize: post.backgroundUrl && '28px',
                            color: post.backgroundUrl && 'white',
                            textAlign: post.backgroundUrl && 'center'
                        }}>{post.content}</p>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            gap: '10px',
                            borderRadius: '15px',
                            border: '1px solid #E5E5E5',
                            overflow: 'hidden',
                            margin: '0px  15px 0px 15px',
                        }}>
                            <div className={`post-media-grid post-media-count-${post.parentPost.mediaList.length} ${post.parentPost.mediaList.length}`}>
                                {post.parentPost.mediaList.map((media, index) => (
                                    <img key={index} src={media.url} alt=""/>
                                ))}
                            </div>
                            <div style={{
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '7px'
                            }}>
                                <div style={{
                                    paddingLeft: '15px'
                                }}>
                                    {getPostHeader(post.parentPost)}
                                </div>
                                <div className="post-body" style={{
                                    backgroundImage: post.parentPost.backgroundUrl && `url(${post.parentPost.backgroundUrl})`,
                                    justifyContent: post.parentPost.backgroundUrl && 'center',
                                    alignItems: post.parentPost.backgroundUrl && 'center',
                                    minHeight: post.parentPost.backgroundUrl && '300px',
                                }}>
                                    <p className="post-content" style={{
                                        padding: post.parentPost.backgroundUrl && '40px',
                                        fontWeight: post.parentPost.backgroundUrl && 'bold',
                                        fontSize: post.parentPost.backgroundUrl && '28px',
                                        color: post.parentPost.backgroundUrl && 'white',
                                        textAlign: post.parentPost.backgroundUrl && 'center'
                                    }}>{post.parentPost.content}</p>
                                </div>
                            </div>
                        </div>
                    </React.Fragment>
                )}
            </div>
        )
    }

    return (
        <div className="post-container">
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '15px'
            }}>
                {getPostHeader(post)}
                {!showCurtain && <FontAwesomeIcon icon={faXmark} fontSize="30px" cursor="pointer"/>}
            </div>
            {getPostBody()}
            <div className="post-bottom">
                {(post.reactionSummary.total !== 0 || post.totalComment !== 0) &&
                    <div className="reactions-comments">
                        <div className="reactions">
                            {post.reactionSummary.emotions.map((emotion, index) => {
                                const reaction = reactions.find(r => r.name === emotion);
                                return (
                                    <img src={reaction.value} alt="" key={index} className="emoji"/>
                                )
                            })}
                            {post.reactionSummary.total > 0 && <p style={{color: 'rgb(145,145,145)', marginLeft: '7px'}}>{post.reactionSummary.total}</p>}
                        </div>
                        <p style={{color: 'rgb(145,145,145)'}}>{post.totalComment > 0 && `${post.totalComment} bình luận`} </p>
                    </div>
                }
                <div className="post-function">
                    <LikeButton reactions={reactions} currentReaction={post.currentUserReaction} setPosts={setPosts} postId={post.id} type={"POST"}/>
                    <div className="function-item" onClick={() => handleClickPost(post)}>
                        <img src="/public/comment-icon.png" alt="" className="emoji"/>
                        <p>Bình luận</p>
                    </div>
                    <div className="function-item" onClick={() => {
                        localStorage.setItem('postId', post.id);
                        setShowCurtain(true);
                        setCreatePost(false);
                        setCurrentPost(false);
                    }}>
                        <img src="/public/share.png" alt="" className="emoji"/>
                        <p>Chia sẻ</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PostContainer;