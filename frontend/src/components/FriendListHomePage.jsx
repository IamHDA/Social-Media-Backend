import React from 'react';
import {getImageMime} from "@/utils/format.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faComment, faHome} from "@fortawesome/free-solid-svg-icons";
import {useNavigate} from "react-router-dom";
import {getConversationIdByRecipientId} from "@/APIs/conversation.js";

const FriendListHomePage = ({friend, index}) => {
    const navigate = useNavigate();

    return (
        <React.Fragment>
            <div style={{
                padding: '10px 0px 10px 0px',
                display: 'flex',
                gap: '10px',
                alignItems: 'center'
            }} key={index}>
                <div style={{
                    position: 'relative'
                }}>
                    <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt="" style={{
                        width: '50px',
                        height: '50px',
                        borderRadius: '50%',
                        objectFit: 'cover'
                    }}/>
                    <div className={`status-dot ${friend.status === "ONLINE" ? "online" : "offline"}`}></div>
                </div>
                <p style={{
                    fontSize: '18px',
                    fontWeight: 'bold'
                }}>{friend.username}</p>
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '5px',
                    marginLeft: 'auto'
                }}>
                    <FontAwesomeIcon icon={faComment} className="friend-list-icon" fontSize="20px" onClick={() => {
                        getConversationIdByRecipientId(friend.id).then(res => {
                            navigate(`/chat/${res}`)
                        })
                    }}/>
                    <FontAwesomeIcon icon={faHome} className="friend-list-icon" fontSize="20px" onClick={() => navigate(`/profile/${friend.id}`)}/>
                </div>
            </div>
        </React.Fragment>
    );
};

export default FriendListHomePage;