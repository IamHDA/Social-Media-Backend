import React, {useEffect, useRef, useState} from 'react';
import {getImageMime} from "../utils/format.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faComment,
    faEllipsis,
    faRightToBracket,
    faShield, faSquareMinus,
    faUser,
    faUserXmark
} from "@fortawesome/free-solid-svg-icons";
import * as participantService from '../APIs/chatParticipant.js';

const ParticipantListChatRoom = ({sendNoticeToUser, sendMessage, index, participant, currentUser, chatRoomRef}) => {
    const containerRef = useRef(null);
    const [choice, setChoice] = useState(false);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (containerRef.current && !containerRef.current.contains(event.target)) {
                setChoice(false)
            }
        };

        if (choice) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [choice]);

    const handleChangeRole = async (role) => {
        try {
            if(role === "MEMBER"){
                const response = await participantService.changeRole(chatRoomRef.current.id, participant.participantId, "MEMBER");
                if(response !== "Role changed") alert("Có lỗi xảy ra");
                else{
                    chatRoomRef.current.participants = chatRoomRef.current.participants.map(p => {
                        if (p.participantId === participant.participantId) return {...p, role: "Thành viên"};
                        return p;
                    });
                    localStorage.setItem("chatRoom", JSON.stringify(chatRoomRef.current));
                    sendMessage(currentUser.nickname + " đã gỡ quyền quản trị viên của " + participant.nickname + ".", "CONVERSATION_NOTICE");
                    setChoice(false);
                }
            }else{
                const response = await participantService.changeRole(chatRoomRef.current.id, participant.participantId, "MOD");
                if(response !== "Role changed") alert("Có lỗi xảy ra");
                else{
                    chatRoomRef.current.participants = chatRoomRef.current.participants.map(p => {
                        if (p.participantId === participant.participantId) return {...p, role: "Quản trị viên"};
                        return p;
                    });
                    localStorage.setItem("chatRoom", JSON.stringify(chatRoomRef.current));
                    sendMessage(currentUser.nickname + " đã thêm " + participant.nickname + " làm quản trị viên nhóm.", "CONVERSATION_NOTICE");
                    setChoice(false);
                }
            }
        }catch (e){
            console.log(e);
        }
    }

    const handleDeleteParticipant = async (participantId) => {
        try {
            const response = await participantService.kickParticipant(chatRoomRef.current.id, participantId);
            if(response !== "Participant deleted") alert("Có lỗi xảy ra");
            else{
                chatRoomRef.current.participants = chatRoomRef.current.participants.filter(p => p.participantId !== participantId);
                if(currentUser.participantId !== participantId) {
                    sendMessage(currentUser.nickname + " đã xóa " + participant.username + " khỏi nhóm.", "CONVERSATION_NOTICE");
                    sendNoticeToUser("bị xóa khỏi nhóm", participantId);
                }
                else sendMessage(currentUser.nickname + " đã rời khỏi nhóm.", "CONVERSATION_NOTICE");
            }
        }catch (e){
            console.log(e);
        }
    }

    return (
        <div className="participant" key={index} ref={containerRef}>
            <div style={{
                display: 'flex',
                gap: '15px',
                alignItems: 'center',
            }}>
                <img src={`data:${getImageMime(participant.avatar)};base64,${participant.avatar}`} alt="" className="participant-avatar"/>
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'center',
                    gap: '7px'
                }}>
                    <p className="participant-name">{participant.username}</p>
                    <p className="participant-role">{participant.role !== "Thành viên" && participant.role}</p>
                </div>
            </div>
            <FontAwesomeIcon icon={faEllipsis} className="participant-more-icon" onClick={() => setChoice(prev => !prev)}/>
            {choice &&
                <div className="participant-option">
                    { (chatRoomRef.current.type === "GROUP" && (currentUser.participantId !== participant.participantId)) &&
                        <div>
                            <FontAwesomeIcon icon={faComment} />
                            <p>Nhắn tin</p>
                        </div>
                    }
                    <div>
                        <FontAwesomeIcon icon={faUser} />
                        <p>Xem trang cá nhân</p>
                    </div>
                    {
                        (chatRoomRef.current.type === "GROUP" && (currentUser.role === "Người tạo nhóm" || currentUser.role === "Quản trị viên")) && (
                            (currentUser.participantId !== participant.participantId) && (
                                (participant.role !== "Người tạo nhóm" && (
                                    (participant.role === "Quản trị viên" && currentUser.role === "Người tạo nhóm") ? (
                                        <div onClick={() => handleChangeRole("MEMBER")}>
                                            <FontAwesomeIcon icon={faSquareMinus} />
                                            <p>Gỡ quyền quản trị viên</p>
                                        </div>
                                    ) : (
                                        <div onClick={() => handleChangeRole("MOD")}>
                                            <FontAwesomeIcon icon={faShield} />
                                            <p>Chỉ định làm quản trị viên</p>
                                        </div>
                                    )
                                ))
                            )
                        )
                    }
                    {chatRoomRef.current.type === "GROUP" && (currentUser.participantId === participant.participantId) ? (
                        <div onClick={() => handleDeleteParticipant(currentUser.id)}>
                            <FontAwesomeIcon icon={faRightToBracket} flip="horizontal" />
                            <p>Rời nhóm</p>
                        </div>
                    ) : (
                        (chatRoomRef.current.type === "GROUP" && (currentUser.role === "Người tạo nhóm" || currentUser.role === "Quản trị viên")) && (
                            (participant.role !== "Người tạo nhóm" && participant.role !== currentUser.role) && (
                                <div onClick={() => handleDeleteParticipant(participant.participantId)}>
                                    <FontAwesomeIcon icon={faUserXmark} />
                                    <p>Xóa thành viên</p>
                                </div>
                            )
                        )
                    )}
                </div>
            }
        </div>
    );
};

export default ParticipantListChatRoom;