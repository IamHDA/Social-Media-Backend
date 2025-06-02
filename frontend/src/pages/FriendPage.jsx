import React, {useContext, useEffect, useState} from 'react';
import {Header} from "@/components/index.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Link, useNavigate} from "react-router-dom";
import {faList, faRightToBracket, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import AuthContext from "@/context/AuthContext.jsx";
import '../styles/Friend.css';
import {getFriendRequests, findFriends} from "@/APIs/friend.js";
import FriendRequestContext from "@/context/FriendRequestContext.jsx";
import {getImageMime} from "@/utils/format.js";
import {getConversationIdByRecipientId} from "@/APIs/conversation.js";

const FriendPage = () => {
    const location = window.location.pathname;
    const navigate = useNavigate();
    const { acceptFriendRequest, declineFriendRequest } = useContext(FriendRequestContext);
    const { user, handleLogout } = useContext(AuthContext);
    const [request, setRequest] = useState(false);
    const [list, setList] = useState(false);
    const [requestList, setRequestList] = useState([]);
    const [friendList, setFriendList] = useState([]);

    useEffect(() => {
        if(user){
            if(location.includes("request")) {
                setRequest(true);
                setList(false);
                getFriendRequests(0, 21).then(res => {
                    setRequestList(res);
                });
            }
            else if(location.includes("list")) {
                setList(true);
                setRequest(false);
                findFriends(user.id, "", 0, 21).then(res => {
                    setFriendList(res);
                });
            }
            else{
                setRequest(false);
                setList(false);
            }
        }

        return () => {
            setRequest(false);
            setList(false);
        }
    }, [location, user]);

    return (
        <div style={{
            display: 'flex',
            gap: '320px'
        }}>
            <div style={{
                position: 'relative',
                height: '100vh',
            }}>
                <Header/>
                <div className="sidebar-left">
                    <h2 style={{
                        margin: '5px 0px 5px 14px'
                    }}>Bạn bè</h2>
                    <Link to={"/friend/request"} className="sidebar-option">
                        <FontAwesomeIcon icon={faUserPlus} />
                        <p>Lời mời kết bạn</p>
                    </Link>
                    <Link to={"/friend/list"} className="sidebar-option">
                        <FontAwesomeIcon icon={faList} />
                        <p>Tất cả bạn bè</p>
                    </Link>
                    <div className="sidebar-option" onClick={() => handleLogout()}>
                        <FontAwesomeIcon icon={faRightToBracket} flip="horizontal" />
                        <p>Đăng xuất</p>
                    </div>
                </div>
            </div>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                marginTop: '75px',
                padding: '20px',
                overflowY: 'auto',
            }}>
                <h2 style={{
                    marginTop: 0
                }}>{request && "Lời mời kết bạn"} {list && "Tất cả bạn bè"}</h2>
                <div className="grid-content">
                    {request &&
                        (requestList.length > 0 && requestList.map((sender, index) => (
                            <div className="content-container" key={index}>
                                <Link to={`/profile/${sender.id}`}>
                                    <img src={`data:${getImageMime(sender.avatar)};base64,${sender.avatar}`} alt=""/>
                                </Link>
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: '10px',
                                    justifyContent: 'center',
                                    padding: '0px 10px 0px 10px',
                                    height: '100%',
                                }}>
                                    <div>
                                        <Link to={`/profile/${sender.id}`} className="content-name">{sender.username}</Link>
                                        <p style={{
                                            fontSize: '14px',
                                            color: 'rgb(163,163,163)',
                                        }}>{sender.sendAt}</p>
                                    </div>
                                    {!sender.status && <button className="confirm-button" onClick={() => acceptFriendRequest(sender, requestList, setRequestList)}>Xác nhận</button>}
                                    <button onClick={() => declineFriendRequest(sender, requestList, setRequestList)} className="cancel-button" style={{
                                        marginTop: sender.status && '40px'
                                    }}>{sender.status ? (sender.status === "accepted" ? "Đã chấp nhận" : "Đã từ chối") : "Xóa"}</button>
                                </div>
                            </div>
                        )))
                    }
                    {list && friendList.length > 0 &&
                        (friendList.map((friend, index) => (
                            <div className="content-container" key={index}>
                                <Link to={`/profile/${friend.id}`}>
                                    <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt=""/>
                                </Link>
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: '10px',
                                    justifyContent: 'center',
                                    padding: '0px 10px 0px 10px',
                                }}>
                                    <Link to={`/profile/${friend.id}`} className="content-name">{friend.username}</Link>
                                    <div style={{
                                        marginTop: '6px',
                                    }}></div>
                                    <button className="confirm-button" onClick={() => {
                                        getConversationIdByRecipientId(friend.id).then(res => {
                                            navigate(`/chat/${res}`)
                                        })
                                    }}>Nhắn tin</button>
                                    <button className="cancel-button">Hủy kết bạn</button>
                                </div>
                            </div>
                        )))
                    }
                </div>
            </div>
        </div>
    );
};

export default FriendPage;