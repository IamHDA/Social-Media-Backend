import React from 'react';
import {getImageMime} from "../utils/format.js"

const ChatRoomList = ({data, index, handleClickChatRoom, user}) => {
    return (
        <div className="chat-room" key={index} onClick={() => handleClickChatRoom(data)}>
            <div style={{
                position: 'relative',
            }}>
                <img src={`data:${getImageMime(data.avatar)};base64,${data.avatar}`} className="chat-room-avatar" alt=""/>
                <div className="status-dot online"/>
            </div>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'flex-start',
                justifyContent: 'center',
                gap: '5px'
            }}>
                <p className="chat-room-name">{data.name ? data.name : data.displayName}</p>
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between'
                }}>
                    {data.lastMessage && (
                        <p className={`last-message ${data.lastMessage.notRead.find(id => id === user.id) && "not-read"}`}>
                            {data.lastMessage.type === "CONVERSATION_NOTICE" ? (
                                data.lastMessage.content
                            ) : (
                                data.lastMessage.senderId !== user.id
                                    ? `${data.lastMessage.senderName}: ${data.lastMessage.content !== "" ? data.lastMessage.content : "Đã gửi file"}`
                                    : `Bạn: ${data.lastMessage.content !== "" ? data.lastMessage.content : "Đã gửi file"}`
                                )
                            }
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ChatRoomList;