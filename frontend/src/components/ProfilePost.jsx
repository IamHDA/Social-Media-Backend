import React, {useEffect, useRef} from 'react';
import {getImageMime} from "@/utils/format.js";
import CreatePostButton from "@/components/CreatePostButton.jsx";
import PostsContainer from "@/components/PostsContainer.jsx";
import {useInView} from "react-intersection-observer";
import {getPostByUserId} from "@/APIs/post.js";
import {useNavigate} from "react-router-dom";

const ProfilePost = ({posts, postedImages, friendList, userInfo, setPosts}) => {
    const pageNumberRef = useRef(1);
    const navigate = useNavigate();
    const { ref, inView } = useInView({});

    useEffect(() => {
        if(inView){
            console.log(pageNumberRef.current);
            pageNumberRef.current += 1;
            getPostByUserId(userInfo.id, pageNumberRef.current).then(res => {
                setPosts(prev => prev.concat(res));
            })
        }
    }, [inView])

    return (
        <div style={{
            display: 'flex',
            gap: '20px',
        }} className="profile-body">
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '10px'
            }}>
                <div className="left-side-container">
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between',
                        width: '400px'
                    }}>
                        <p style={{
                            fontSize: '24px',
                            fontWeight: 'bold'
                        }}>Ảnh</p>
                        <p style={{
                            color: '#E53935',
                            cursor: 'pointer'
                        }}>Xem tất cả</p>
                    </div>
                    <div className="x3-grid-container image">
                        {postedImages.map((image, index) => (
                            <img src={image.url} alt="" style={{
                                width: '130px',
                                height: '130px',
                                borderRadius: '10px'
                            }} key={index}/>
                        ))}
                    </div>
                </div>
                <div className="left-side-container">
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between'
                    }}>
                        <p style={{
                            fontSize: '24px',
                            fontWeight: 'bold'
                        }}>Bạn bè</p>
                        <p style={{
                            color: '#E53935',
                            cursor: 'pointer'
                        }}>Xem tất cả</p>
                    </div>
                    <div className="x3-grid-container friend">
                        {friendList.map((friend, index) => (
                            <div style={{
                                cursor: 'pointer'
                            }} key={index} onClick={() => {
                                navigate(`/profile/${friend.id}`);
                            }}>
                                <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt="" style={{
                                    width: '130px',
                                    height: '130px',
                                    borderRadius: '10px'
                                }}/>
                                <p style={{
                                    fontSize: '15px',
                                    fontWeight: 'bold'
                                }}>{friend.username}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div style={{
                display: 'flex',
                flex: '1',
                flexDirection: 'column',
                gap: '10px'
            }}>
                <CreatePostButton opponent={userInfo} setPosts={setPosts}/>
                <PostsContainer posts={posts} setPosts={setPosts} ref={ref}/>
            </div>
        </div>
    );
};

export default ProfilePost;