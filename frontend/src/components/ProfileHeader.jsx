import React, {useContext, useEffect, useRef, useState} from 'react';
import {getImageMime} from "@/utils/format.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faCamera,
    faComment,
    faPen,
    faUserCheck,
    faUserPlus, faUserXmark,
    faXmark
} from "@fortawesome/free-solid-svg-icons";
import * as userService from '../APIs/user.js';
import {
    checkFriend,
    checkFriendRequest,
    sendFriendRequest,
    deleteFriend,
    declineFriendRequest,
    acceptFriendRequest
} from "@/APIs/friend.js";
import AuthContext from "@/context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";

const ProfileHeader = ({isMine, userInfo, setUserInfo, selectedChoice}) => {
    const {user, setUser} = useContext(AuthContext);
    const showOptionRef = useRef(null);
    const [editBio, setEditBio] = useState(false);
    const [isFriend, setIsFriend] = useState(false);
    const [showOption, setShowOption] = useState(false);
    const responseOptionRef = useRef(0);
    const [friendRequest, setFriendRequest] = useState(null);
    const [bio, setBio] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if(userInfo) setBio(userInfo.bio);
        if(!isMine) {
            checkFriendRequest(userInfo.id).then(response => {
                console.log(response);
                setFriendRequest(response);
            })
            checkFriend(userInfo.id).then(response => {
                setIsFriend(response);
            })
        }
    }, [userInfo])

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (showOptionRef.current && !showOptionRef.current.contains(event.target)) {
                setShowOption(false);
            }
        };

        if (showOption) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showOption]);

    const handleChangeAvatar = async (image) => {
        try {
            const formData = new FormData();
            const reader = new FileReader();
            formData.append('file', image);
            const response = await userService.updateAvatar(formData);
            if(response === "Change Avatar Successfully!"){
                reader.onloadend = function () {
                    const base64data = reader.result.split(',')[1];
                    setUserInfo(prev => ({ ...prev, avatar: base64data }));
                    setUser(prev => ({ ...prev, avatar: base64data }));
                }
                reader.readAsDataURL(image);
            }

        }catch (e){
            console.log(e);
        }
    }

    const handleChangeBackground = async (image) => {
        try {
            const formData = new FormData();
            const reader = new FileReader();
            formData.append('file', image);
            const response = await userService.updateBackgroundImage(formData);
            if(response === "Change Background Image Successfully!"){
                reader.onloadend = function () {
                    const base64data = reader.result.split(',')[1];
                    setUserInfo(prev => ({ ...prev, background: base64data }));
                }
                reader.readAsDataURL(image);
            }

        }catch (e){
            console.log(e);
        }
    }

    const handleSubmitBio = async () => {
        try {
            const response = await userService.editBio(bio);
            if(response === "Bio changed"){
                setUserInfo(prev => ({ ...prev, bio: bio }));
            }
        }catch (e){
            console.log(e);
        }
    }

    const handleClickFriendButton = async () => {
        try {
            if(!showOption && friendRequest) setShowOption(true);
            else if(responseOptionRef.current === 1){
                const response = await declineFriendRequest(friendRequest.senderId, friendRequest.recipientId);
                if(response !== "Friend request deleted"){
                    alert("Có lỗi xảy ra");
                    return;
                }
                responseOptionRef.current = 0;
                setFriendRequest(null);
            }else if(responseOptionRef.current === 2){
                const response = await acceptFriendRequest(friendRequest.senderId);
                if(response !== "New friend request accepted"){
                    alert("Có lỗi xảy ra");
                    return;
                }
                setIsFriend(true);
                responseOptionRef.current = 0;
                setFriendRequest(null);
            }else if(isFriend){
                const response = await deleteFriend(userInfo.id);
                if(response !== "Friend deleted") alert("Có lỗi xảy ra");
                setIsFriend(false);
            }else{
                const response = await sendFriendRequest(userInfo.id);
                if(response !== "Request sent") alert("Có lỗi xảy ra");
                setFriendRequest({
                    senderId: user.id,
                    recipientId: userInfo.id
                })
            }
        }catch (e){
            console.log(e);
        }
    }

    return (
        <div>
            <div style={{
                position: 'relative'
            }}>
                <img className="background-image" src={`data:${getImageMime(userInfo.background)};base64,${userInfo.background}`} alt=""/>
                {isMine &&
                    <label htmlFor="background-image-input" className="modify-background-button">
                        <FontAwesomeIcon icon={faCamera} fontSize="20px"/>
                        <p style={{
                            fontSize: '18px',
                            fontWeight: 'bold'
                        }}>Chỉnh sửa ảnh bìa</p>
                        <input
                            type="file"
                            id="background-image-input"
                            hidden
                            accept="image/*"
                            onChange={(e) => handleChangeBackground(e.target.files[0])}
                        />
                    </label>
                }
                <div style={{
                    position: 'absolute',
                    top: '84%',
                    left: '30px',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '20px'
                }}>
                    <div style={{ position: 'relative' }}>
                        <img
                            className="profile-avatar"
                            src={`data:${getImageMime(userInfo.avatar)};base64,${userInfo.avatar}`}
                            alt=""
                        />
                        {isMine && (
                            <label htmlFor="profile-avatar" style={{
                                position: 'absolute',
                                bottom: '15px',
                                right: '15px',
                                padding: '10px',
                                borderRadius: '50%',
                                backgroundColor: 'white',
                                cursor: 'pointer'
                            }}>
                                <FontAwesomeIcon icon={faCamera} fontSize="20px" />
                                <input
                                    type="file"
                                    id="profile-avatar"
                                    hidden
                                    accept="image/*"
                                    onChange={(e) => handleChangeAvatar(e.target.files[0])}
                                />
                            </label>
                        )}
                    </div>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        marginTop: '60px',
                        justifyContent: 'center',
                        alignItems: 'flex-start',
                        gap: '5px',
                        width: '400px',
                    }}>
                        <p style={{
                            color: 'black',
                            fontWeight: 'bold',
                            fontSize: '26px',
                            margin: 0
                        }}>
                            {userInfo.username}
                        </p>
                        <p style={{
                            color: 'rgb(163,163,163)',
                            fontWeight: 'bold',
                            fontSize: '17px',
                            margin: 0
                        }}>
                            {userInfo.numberOfFriends} người bạn
                        </p>
                        <div style={{
                            marginTop: '5px',
                            position: 'relative'
                        }}>
                            {!editBio ? (
                                <p style={{
                                    fontSize: '17px',
                                    display: '-webkit-box',
                                    WebkitLineClamp: 3,
                                    maxWidth: '300px',
                                    WebkitBoxOrient: 'vertical',
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                    margin: 0
                                }}>
                                    {userInfo.bio}
                                </p>
                            ) : (
                                <textarea
                                    value={bio}
                                    style={{
                                        width: '300px',
                                        resize: 'none',
                                        fontSize: '17px',
                                        marginBottom: '-30px'
                                    }}
                                    rows={3}
                                    maxLength={104}
                                    onChange={(e) => setBio(e.target.value)}
                                    onKeyDown={(e) => {
                                        const currentLines = bio.split('\n').length;
                                        if(e.key === "Enter" && !e.shiftKey){
                                            e.preventDefault();
                                            handleSubmitBio().then(() => {
                                                setEditBio(false);
                                            });
                                        }
                                        if (e.key === 'Enter' && currentLines >= 3) {
                                            e.preventDefault();
                                        }
                                    }}
                                />
                            )}
                            {isMine && (
                                <React.Fragment>
                                    <svg
                                        width="80"
                                        height="20"
                                        style={{
                                            position: 'absolute',
                                            top: '-20px',
                                            right: '-35px',
                                            pointerEvents: 'none',
                                            display: (!bio || bio.length === 0) && 'none'
                                        }}
                                    >
                                        <line
                                            x1="0"
                                            y1="60"
                                            x2="60"
                                            y2="0"
                                            stroke="gray"
                                            strokeWidth="1"
                                            strokeDasharray="4 4"
                                        />
                                    </svg>
                                    <div style={{
                                        position: 'absolute',
                                        top: (!bio || bio.length === 0) ? '-20px' : '-40px',
                                        right: (!bio || bio.length === 0) ? '-35px' : '-35px',
                                        padding: '5px',
                                        border: '1px solid gray',
                                        backgroundColor: 'white',
                                        borderRadius: '50%',
                                        cursor: 'pointer'
                                    }}
                                         title="Chỉnh sửa tiểu sử"
                                    >
                                        {!editBio && <FontAwesomeIcon icon={faPen} style={{ color: 'gray' }} onClick={() => setEditBio(true)}/>}
                                        {editBio && <FontAwesomeIcon icon={faXmark} style={{ color: 'black' }} onClick={() => {
                                            setBio(userInfo.bio);
                                            setEditBio(false);
                                        }}/>}
                                    </div>
                                </React.Fragment>
                            )}
                        </div>
                    </div>
                </div>
            </div>
            <div style={{
                marginTop: '30px',
                display: 'flex',
                flexDirection: 'column',
                width: 'auto',
                gap: '35px'
            }} className="profile-body">
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr',
                    gap: '10px',
                    alignSelf: 'flex-end'
                }}>
                    {!isMine && (
                        <React.Fragment>
                            <div style={{
                                position: 'relative'
                            }}>
                                <div className="profile-function" onClick={handleClickFriendButton} style={{
                                    backgroundColor: 'white',
                                    border: '2px solid #E53935'
                                }}>
                                    {friendRequest ? (
                                        friendRequest.senderId === user.id ? (
                                            <React.Fragment>
                                                <FontAwesomeIcon icon={faUserXmark} />
                                                <p>Xóa lời mời</p>
                                            </React.Fragment>
                                        ) : (
                                            <React.Fragment>
                                                <FontAwesomeIcon icon={faUserCheck} />
                                                <p>Phản hồi</p>
                                            </React.Fragment>
                                        )
                                    ) : (
                                        isFriend ? (
                                            <React.Fragment>
                                                <FontAwesomeIcon icon={faUserXmark} />
                                                <p>Hủy kết bạn</p>
                                            </React.Fragment>
                                        ) : (
                                            <React.Fragment>
                                                <FontAwesomeIcon icon={faUserPlus} />
                                                <p>Thêm bạn bè</p>
                                            </React.Fragment>
                                        )
                                    )}
                                </div>
                                {showOption && (
                                    <div
                                        ref={showOptionRef}
                                        style={{
                                            display: 'flex',
                                            flexDirection: 'column',
                                            gap: '5px',
                                            position: 'absolute',
                                            top: '100%',
                                            left: '0',
                                            right: '0',
                                            backgroundColor: 'rgb(209,209,209)',
                                            borderRadius: '10px',
                                            padding: '10px',
                                            zIndex: '100',
                                            cursor: 'pointer'
                                    }}>
                                        <p className="response-text" onClick={() => {
                                            responseOptionRef.current = 2;
                                            handleClickFriendButton();
                                            setShowOption(false);
                                        }}>Chấp nhận lời mời</p>
                                        <p className="response-text" onClick={() => {
                                            responseOptionRef.current = 1;
                                            handleClickFriendButton();
                                            setShowOption(false);
                                        }}>Từ chối lời mời</p>
                                    </div>
                                )}
                            </div>
                            <div className="profile-function" style={{
                                backgroundColor: 'white',
                                border: '2px solid #E53935',
                            }}>
                                <FontAwesomeIcon icon={faComment} />
                                <p>Nhắn tin</p>
                            </div>
                        </React.Fragment>
                    )}
                    <button className={`profile-choice ${selectedChoice === 0 && 'selected'}`} onClick={() => navigate(`/profile/${userInfo.id}`)}>Bài viết</button>
                    <button className={`profile-choice ${selectedChoice === 1 && 'selected'}`} onClick={() => navigate(`/profile/${userInfo.id}/friend`)}>Bạn bè</button>
                    <button className={`profile-choice ${selectedChoice === 2 && 'selected'}`} onClick={() => navigate(`/profile/${userInfo.id}/photo`)}>Ảnh</button>
                    <button className={`profile-choice ${selectedChoice === 3 && 'selected'}`} onClick={() => navigate(`/profile/${userInfo.id}/video`)}>Video</button>
                </div>
                <div style={{
                    border: '1px solid lightgray'
                }}></div>
            </div>
        </div>
    );
};

export default ProfileHeader;