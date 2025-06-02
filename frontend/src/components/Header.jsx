import React, {useContext, useEffect, useRef, useState} from 'react';
import '../styles/Header.css';
import AuthContext from "@/context/AuthContext.jsx";
import {getImageMime} from "../utils/format.js";
import {faBell, faComment, faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {getNotifications} from "@/APIs/notification.js";
import {useDebounce} from "@/hooks/useDebounce.js"
import {searchUser} from "@/APIs/user.js";

const Header = () => {
    const {user} = useContext(AuthContext);
    const notificationRef = useRef(null);
    const searchListRef = useRef(null);
    const [showNotification, setShowNotification] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const [isSearch, setIsSearch] = useState(false);
    const [searchList, setSearchList] = useState([]);
    const searchDebounce = useDebounce(searchInput, 400);

    useEffect(() => {
        searchUser(searchDebounce).then(res => {
            setSearchList(res);
        })
    }, [searchDebounce]);

    useEffect(() => {
        if(showNotification) {
            console.log("show notification");
            getNotifications(6, 1).then(res => {
                setNotifications(res);
            })
        }
    }, [showNotification]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (notificationRef.current && !notificationRef.current.contains(event.target)) {
                setShowNotification(false);
            }
            if (searchListRef.current && !searchListRef.current.contains(event.target)) {
                setIsSearch(false);
            }
        };

        if (showNotification || isSearch) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showNotification, isSearch]);


    return user && (
        <React.Fragment>
            <div className="header">
                <svg className="header-logo" viewBox="0 0 600 200" xmlns="http://www.w3.org/2000/svg" onClick={() => window.location.href = "/"}>
                    <g fill="#E53935" stroke="#E53935" strokeWidth="6" strokeLinecap="round">
                        <circle cx="80" cy="100" r="20"/>

                        <circle cx="40" cy="100" r="10"/>
                        <circle cx="105" cy="60" r="10"/>
                        <circle cx="105" cy="140" r="10"/>

                        <path d="M50 100 A30 30 0 0 1 80 70
                         M80 70 A30 30 0 0 1 110 100
                         M110 100 A30 30 0 0 1 80 130
                         M80 130 A30 30 0 0 1 50 100"
                              fill="none"/>

                        <line x1="80" y1="80" x2="105" y2="60"/>
                        <line x1="80" y1="120" x2="105" y2="140"/>
                        <line x1="60" y1="100" x2="40" y2="100"/>
                    </g>
                    <text x="150" y="95" fontFamily="Arial" fontSize="48" fill="#E53935" fontWeight="bold" dominantBaseline="middle">PTIT</text>
                    <text x="150" y="140" fontFamily="Arial" fontSize="32" fill="#E53935" dominantBaseline="middle">CONNECT</text>
                </svg>
                <div style={{
                    position: 'relative',
                    display: 'flex',
                    gap: '10px',
                    alignItems: 'center',
                    padding: '8px',
                    borderRadius: '20px',
                    alignSelf: 'center',
                    width: '350px',
                    backgroundColor: '#d5d5d5',
                }}>
                    <FontAwesomeIcon icon={faMagnifyingGlass} fontSize="20px"/>
                    <input
                        value={searchInput}
                        placeholder="Tìm kiếm trên PTIT CONNECT"
                        onClick={() => setIsSearch(true)}
                        onChange={e => setSearchInput(e.target.value)}
                        style={{
                            border: 'none',
                            outline: 'none',
                            backgroundColor: 'transparent',
                            fontSize: '18px',
                            width: '100%'
                    }}/>
                </div>
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '15px',
                    position: 'relative'
                }}>
                    <FontAwesomeIcon icon={faComment} className="icon-gray-background" fontSize="25px" onClick={() => window.location.href = "/chat"} style={{}}/>
                    <FontAwesomeIcon icon={faBell} className="icon-gray-background" fontSize="25px" onClick={() => setShowNotification(prev => !prev)}/>
                    <div className="user-header-container">
                        <img src={`data:${getImageMime(user.avatar)};base64,${user.avatar}`} className="user-avatar" alt=""/>
                        <p style={{fontSize: '17px', fontWeight: 'bold'}}>{user.username}</p>
                    </div>
                </div>
            </div>
            {isSearch && searchList.length > 0 && (
                <div className="search-list"
                     ref={searchListRef}
                >
                    {searchList.map((user, index) => (
                        <div key={index} className="search-list-item"
                             onClick={() => {
                                 window.location.href = `/profile/${user.id}`;
                                 setIsSearch(false);
                             }}>
                            <img src={`data:${getImageMime(user.avatar)};base64,${user.avatar}`} alt="" className="user-avatar" style={{}}/>
                            <p style={{
                                fontSize: '17px',
                                fontWeight: 'bold'
                            }}>{user.username}</p>
                        </div>
                    ))}
                </div>
            )}
            {showNotification &&
                <div className="notification-container" ref={notificationRef}>
                    <h2 style={{
                        margin: 0,
                        alignSelf: 'flex-start',
                    }}>Thông báo</h2>
                    {notifications && notifications.map((notification, index) => (
                        <div className="notification" key={index}>
                            <div style={{
                                display: 'flex',
                                alignItems: 'center',
                                gap: '10px'
                            }}>
                                <img src={`data:${getImageMime(notification.author.avatar)};base64,${notification.author.avatar}`} alt="" style={{
                                    width: '60px',
                                    height: '60px',
                                    borderRadius: '50%',
                                    objectFit: 'cover'
                                }}/>
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: '3px'
                                }}>
                                    <p className="notification-content">
                                        <span style={{
                                        fontWeight: 'bold'
                                        }}>
                                            {notification.author.username}
                                        </span>
                                        &nbsp;
                                        {notification.content}
                                    </p>
                                    <p style={{
                                        fontWeight: 'bold',
                                        color: '#E53935'
                                    }}>{notification.noticeAt}</p>
                                </div>
                            </div>
                            <div className={`notice-status ${!notification.read && "unread"}`}></div>
                        </div>
                    ))}
                </div>
            }
        </React.Fragment>
    );
};

export default Header;