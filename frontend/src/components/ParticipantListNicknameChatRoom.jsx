import React, {useContext, useEffect, useRef, useState} from 'react';
import {getImageMime} from "../utils/format.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {changeNickname} from '../APIs/chatParticipant.js';

const ParticipantListNicknameChatRoom = ({user, participant, optionInput, setOptionInput, index, chatRoomRef, setMessages, sendMessage, setChatRooms}) => {
    const [modify, setModify] = useState(false);
    const containerRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (containerRef.current && !containerRef.current.contains(event.target)) {
                setModify(false);
            }
        };

        if (modify) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [modify]);

    return (
        <div className={`participant-nickname-div ${!modify && "hover"}`} key={index} ref={containerRef} onClick={() => setModify(true)}>
            <div style={{display: 'flex', alignItems: 'center', gap: '10px'}}>
                <img
                    style={{
                        width: '45px',
                        height: '45px',
                        borderRadius: '50%',
                        objectFit: 'cover'
                    }}
                    src={`data:${getImageMime(participant.avatar)};base64,${participant.avatar}`}
                    alt=""
                />
                {!modify ? (
                    <div style={{display: 'flex', flexDirection: 'column', gap: '3px'}}>
                        <p style={{fontSize: '18px', fontWeight: 'bold'}}>{participant.username}</p>
                        <p>{participant.nickname !== participant.username ? participant.nickname : "Đặt biệt danh"}</p>
                    </div>
                ) : (
                    <input
                        type="text"
                        value={optionInput}
                        onChange={(e) => setOptionInput(e.target.value)}
                        maxLength={30}
                        placeholder={participant.nickname}
                        style={{
                            width: '325px',
                            border: '1px solid lightgray',
                            fontSize: '18px',
                            padding: '10px',
                            borderRadius: '5px',
                            color: "inherit",
                        }}
                    />
                )}
            </div>
            {modify
                ? <FontAwesomeIcon icon={faCheck} className="nickname-icon" onClick={async () => {
                    try {
                        const response = await changeNickname(chatRoomRef.current.id, participant.participantId, optionInput);
                        if(response !== "Nickname changed") alert("Có lỗi xảy ra");
                        else{
                            if(optionInput === participant.username || optionInput === "") return;
                            const updatedChatRoom = {
                                ...chatRoomRef.current,
                                displayName: optionInput,
                                participants: chatRoomRef.current.participants.map(p =>
                                    p.participantId === participant.participantId ? { ...p, nickname: optionInput } : p
                                )
                            };
                            const targetParticipant = chatRoomRef.current.participants.find(
                                p => p.participantId === participant.participantId
                            );
                            if(chatRoomRef.current.type === "PRIVATE"){
                                setChatRooms(prev => prev.map(room =>
                                    room.id === updatedChatRoom.id ? updatedChatRoom : room
                                ));
                            }
                            setMessages(prev => prev.map(message => {
                                if (message.sender.id === targetParticipant.participantId) {
                                    const newMessage = {
                                        ...message,
                                        sender: {
                                            ...message.sender,
                                            username: optionInput
                                        }
                                    };
                                    console.log(newMessage);
                                    return newMessage;

                                }
                                return message;
                            }));
                            chatRoomRef.current = updatedChatRoom;
                            if(chatRoomRef.current.type === "GROUP") sendMessage(user.username + " đã đổi biệt danh của " + participant.username + " thành " + optionInput + ".", "CONVERSATION_NOTICE");
                            else {
                                let content = "";
                                if(participant.participantId === user.id) content = user.username + " đã đổi biệt danh của mình thành " + optionInput + ".";
                                else content = user.username + " đã đổi biệt danh của " + participant.username + " thành " + optionInput + ".";
                                sendMessage(content, "CONVERSATION_NOTICE");
                            }
                            setOptionInput("");
                            setModify(false);
                        }
                    }catch (e){
                        console.log(e);
                    }
                }}/>
                : <FontAwesomeIcon icon={faPenToSquare} className="nickname-icon"/>
            }
        </div>
    );
};

export default ParticipantListNicknameChatRoom;
