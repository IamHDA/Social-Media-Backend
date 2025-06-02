import React, {useContext, useEffect, useRef, useState} from 'react';
import { Header } from "../components/index.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faComment, faMagnifyingGlass, faRightToBracket, faUser, faUserGroup} from "@fortawesome/free-solid-svg-icons";
import {getImageMime} from "@/utils/format.js";
import AuthContext from "@/context/AuthContext.jsx";
import * as friendService from "@/APIs/friend.js";
import {getNewestPost} from "@/APIs/post.js";
import '../styles/HomePage.css'
import {useDebounce} from "../hooks/useDebounce.js";
import FriendListHomePage from "@/components/FriendListHomePage.jsx";
import {Link} from "react-router-dom";
import PostsContainer from "@/components/PostsContainer.jsx";
import CreatePostButton from "@/components/CreatePostButton.jsx";
import CurtainContext from "@/context/CurtainContext.jsx";
import {useInView} from "react-intersection-observer";
import {acceptFriendRequest, declineFriendRequest} from "@/APIs/friend.js";

const HomePage = () => {
    const {user, handleLogout} = useContext(AuthContext);
    const {showCurtain} = useContext(CurtainContext);
    const [posts, setPosts] = useState([]);
    const [isSearching, setIsSearching] = useState(false);
    const [friendRequests, setFriendRequests] = useState([]);
    const [friendList, setFriendList] = useState([]);
    const [friendListSearch, setFriendListSearch] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const searchDebounce = useDebounce(searchInput, 300);
    const { ref, inView } = useInView({});
    const pageNumberRef = useRef(1);

    useEffect(() => {
        if(inView){
            pageNumberRef.current += 1;
            getNewestPost(pageNumberRef.current).then(res => {
                setPosts(prev => prev.concat(res));
            })
        }
    }, [inView])

    useEffect(() => {
        const fetchData = async () => {
            setPosts(await getNewestPost(1));
            setFriendRequests(await fetchFriendRequests());
            setFriendList(await fetchAllFriends());
        }
        fetchData();
    }, [user])

    useEffect(() => {
        if(searchInput !== ""){
            setIsSearching(true);
            fetchAllFriends().then(res => {
                setFriendListSearch(res);
            });
        }else{
            setIsSearching(false);
        }
    }, [searchDebounce])

    const handleAcceptFriendRequest = async (userId) => {
        try {
            const response = await acceptFriendRequest(userId);
            if (response === "New friend request accepted") {
                setFriendRequests(prev => prev.map(friend => {
                    if (friend.userId === userId) {
                        return {
                            ...friend,
                            status: 'accepted'
                        };
                    }
                    return friend;
                }));
            }else{
                alert("Có lỗi xảy ra");
            }
        }catch (e) {
            console.log(e);
            alert("Có lỗi xảy ra");
        }
    }

    const handleDeclineFriendRequest = async (userId) => {
        try {
            const response = await declineFriendRequest(userId, user.id);
            if (response === "Friend request deleted") {
                setFriendRequests(prev => prev.map(friend => {
                    if (friend.userId === userId) {
                        return {
                            ...friend,
                            status: 'declined'
                        };
                    }
                    return friend;
                }));
            }
        }catch (e){
            console.log(e);
            alert("Có lỗi xảy ra");
        }
    }

    const fetchFriendRequests = async () => {
        return await friendService.getFriendRequests(0, 4);
    }

    const fetchAllFriends = async () => {
        return await friendService.findFriends(user.id, searchInput, 0, 15);
    }

    const showRequestWithStatus = (request) => {
        setTimeout(async () => {
            const response1 = await fetchFriendRequests();
            const response2 = await fetchAllFriends();
            setFriendRequests(response1);
            setFriendList(response2);
        }, [2500]);
        return (request.status === "accepted" ?
                <p className="status-text">Đã chấp nhận lời mời</p> :
                <p className="status-text">Đã từ chối lới mời</p>
        )
    }

    return user && (
        <div style={{
            height: '100vh',
            position: 'relative',
        }}>
            {showCurtain && <div className="curtain-background"></div>}
            <Header/>
            <div className="sidebar-left">
                <Link to={`/profile/${user.id}`} className="sidebar-option">
                    <FontAwesomeIcon icon={faUser} />
                    <p>Trang cá nhân</p>
                </Link>
                <Link to="/friend" className="sidebar-option">
                    <FontAwesomeIcon icon={faUserGroup} />
                    <p>Bạn bè</p>
                </Link>
                <Link to="/chat" className="sidebar-option">
                    <FontAwesomeIcon icon={faComment} />
                    <p>Nhắn tin</p>
                </Link>
                <div className="sidebar-option" onClick={handleLogout}>
                    <FontAwesomeIcon icon={faRightToBracket} flip="horizontal" />
                    <p>Đăng xuất</p>
                </div>
            </div>
            <div className="center-body">
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '20px',
                    width: '700px',
                    margin: '0 auto',
                    marginTop: '90px',
                }}>
                    <CreatePostButton opponent={user} setPosts={setPosts}/>
                    <PostsContainer posts={posts} setPosts={setPosts} ref={ref}/>
                </div>
            </div>
            <div className="sidebar-right">
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'center'
                }}>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                    }}>
                        <h3>Lời mời kết bạn</h3>
                        <Link to="/friend/request" className="link">Tất cả</Link>
                    </div>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '10px',
                        height: 'auto',
                        maxHeight: '330px'
                    }}>
                        {friendRequests.length > 0 ? friendRequests.map((friend, index) => (
                                <div className="friend-request-container" key={index}>
                                    {/*{console.log(friend)}*/}
                                    <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt="" style={{
                                        width: '55px',
                                        height: '55px',
                                        borderRadius: '50%',
                                        objectFit: 'cover'
                                    }}/>
                                    <div style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        justifyContent: 'center',
                                        gap: '5px',
                                        width: '100%'
                                    }}>
                                        <div style={{
                                            display: 'flex',
                                            justifyContent: 'space-between',
                                            alignItems: 'center'
                                        }}>
                                            <p className="friend-request-sender-name">{friend.username}</p>
                                            <p style={{color: 'lightgray', fontSize: '15px'}}>{friend.sendAt}</p>
                                        </div>
                                        {!friend.status ? (
                                            <div style={{
                                                display: 'flex',
                                                gap: '10px',
                                                width: '100%'
                                            }}>
                                                <button className="confirm-button" onClick={() => handleAcceptFriendRequest(friend.userId)}>
                                                    Xác nhận
                                                </button>
                                                <button className="cancel-button" onClick={() => handleDeclineFriendRequest(friend.userId)}>
                                                    Xóa
                                                </button>
                                            </div>
                                        ) : showRequestWithStatus(friend)}
                                    </div>
                                </div>
                            )) :
                            <div style={{
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'center',
                                alignItems: 'center',
                                height: '100%',
                                gap: '10px',
                                marginTop: '-10px'
                            }}>
                                <img src="/public/no-request.png" alt="" style={{
                                    width: '170px',
                                    height: '170px',
                                    borderRadius: '50%',
                                    objectFit: 'cover',
                                    marginBottom: '-30px'
                                }}/>
                                <p>Bạn chưa có lời mời kết bạn nào</p>
                            </div>
                        }
                    </div>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between'
                    }}>
                        <h3>Bạn bè</h3>
                        <Link to="/friend/list" className="link" style={{
                            alignSelf: 'center'
                        }}>Tất cả</Link>
                    </div>
                    <div className="search-bounding">
                        <FontAwesomeIcon icon={faMagnifyingGlass} fontSize="18px"/>
                        <input
                            type="text"
                            placeholder="Tìm kiếm bạn bè"
                            value={searchInput}
                            onChange={(e) => setSearchInput(e.target.value)}
                        />
                    </div>
                    {
                        isSearching ? (
                            friendListSearch.length > 0 && (
                                friendListSearch.map((friend, index) => (
                                    <FriendListHomePage key={index} friend={friend} index={index} />
                                ))
                            )
                        ) : (
                            friendList.length > 0 ? (
                                friendList.map((friend, index) => (
                                    <FriendListHomePage key={index} friend={friend} index={index} />
                                ))
                            ) : (
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    height: '100%',
                                    gap: '10px',
                                }}>
                                    <img src="/public/no-request.png" alt="" style={{
                                        width: '170px',
                                        height: '170px',
                                        borderRadius: '50%',
                                        objectFit: 'cover',
                                        marginBottom: '-30px'
                                    }}/>
                                    <p>Bạn chưa có người bạn nào</p>
                                    <a href="" style={{ textAlign: 'center' }}>
                                        Ấn vào đây để tìm <br />những người bạn mới
                                    </a>
                                </div>
                            )
                        )
                    }
                </div>
            </div>
        </div>
    );
};

export default HomePage;