import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import {
    faArrowLeft,
    faCamera,
    faChevronDown,
    faChevronLeft,
    faChevronUp,
    faCircleInfo,
    faFile,
    faFileImage,
    faFileLines, faHouse,
    faImage,
    faMagnifyingGlass,
    faPen,
    faPenToSquare,
    faPhone,
    faPlus,
    faUserPlus,
    faVideo,
    faX,
    faXmark
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import '../styles/Chat.css';
import SockJSContext from "@/context/SockJSContext.jsx";
import * as conversationService from '../APIs/conversation.js';
import * as chatService from '../APIs/message.js';
import * as messageService from '../APIs/message.js';
import * as friendService from '../APIs/friend.js';
import * as participantService from '../APIs/chatParticipant.js';
import {getImageMime} from "../utils/format.js";
import AuthContext from "@/context/AuthContext.jsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import ParticipantListNicknameChatRoom from "@/components/ParticipantListNicknameChatRoom.jsx";
import ParticipantListChatRoom from "@/components/ParticipantListChatRoom.jsx";
import FriendListChat from "@/components/FriendListChat.jsx";
import {useInView} from "react-intersection-observer";
import {useDebounce} from '../hooks/useDebounce.js';
import ChatRoomList from "@/components/ChatRoomList.jsx";

const Chat = () => {
    const {chatId} = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const {stompClientRef, setUpStompClient, disconnectStomp, unsubscribe} = useContext(SockJSContext);
    const {user} = useContext(AuthContext);
    const chatRoomRef = useRef(null);
    const messageEndRef = useRef(null);
    const userRef = useRef(null);
    const currentParticipantRef = useRef(null);
    const friendPageNumberRef = useRef(0);
    const filePageNumberRef = useRef(0);
    const containerRef = useRef(null);
    const fileTypeRef = useRef([]);
    const mediaListRef = useRef([]);
    const showApplicationRef = useRef(false);
    const showMediaRef = useRef(false);
    const chatRoomsRef = useRef([]);
    const [chosenFriends, setChosenFriends] = useState([]);
    const [creatingGroup, setCreatingGroup] = useState(false);
    const [chatRooms, setChatRooms] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [mediaList, setMediaList] = useState([]);
    const [showInfo, setShowInfo] = useState(false);
    const [messages, setMessages] = useState([]);
    const [filterChoice, setFilterChoice] = useState(0);
    const [showedFile, setShowedFile] = useState([]);
    const [showMedia, setShowMedia] = useState(false);
    const [showApplication, setShowApplication] = useState(false);
    const [chosenParticipants, setChosenParticipants] = useState([]);
    const [option1, setOption1] = useState(false);
    const [option2, setOption2] = useState(false);
    const [option3, setOption3] = useState(false);
    const [option11, setOption11] = useState(false);
    const [option13, setOption13] = useState(false);
    const [option21, setOption21] = useState(false);
    const [option312, setOption312] = useState(false);
    const [userList, setUserList] = useState([]);
    const [showUserList, setShowUserList] = useState(false);
    const [optionInput, setOptionInput] = useState("");
    const [beingKicked, setBeingKicked] = useState(false);
    const [searchingConversation, setSearchingConversation] = useState(false);
    const [searchConversationInput, setSearchConversationInput] = useState("");
    const [searchConversationResponse, setSearchConversationResponse] = useState([]);
    const [friendNameInput, setFriendNameInput] = useState("");
    const participantInputDebounce = useDebounce(friendNameInput, 300);
    const searchConversationDebounce = useDebounce(searchConversationInput, 300);
    const [tmpChatRoomName, setTmpChatRoomName] = useState("");
    const [tmpChatRoomAvatar, setTmpChatRoomAvatar] = useState(null);
    const {ref: loadMoreFriendRef, inView: inViewFriend} = useInView({});
    const {ref: loadMoreFileRef, inView: inViewFile} = useInView({});

    useEffect(() => {
        const fetchConversation = async () => {
            try {
                if(!filterChoice) setChatRooms(await fetchChatRooms());
                else setChatRooms(await conversationService.getNotReads());
            }catch (e){
                console.log(e);
            }
        }
        fetchConversation();
    }, [filterChoice])

    useEffect(() => {
        setUserList([]);
        const fetchData = async () => {
            if(user){
                userRef.current = user;
                const response = await fetchChatRooms();
                chatRoomsRef.current = response;
                setChatRooms(response);
                const chatRoomIds = response.map(chatRoom => chatRoom.id);
                await setUpStompClient(chatRoomIds, user.id, onMessageReceived, onNoticeReceived);
            }
        }
        fetchData();

        return () => {
            disconnectStomp();
        }
    }, [user])

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const response = await messageService.getMessages(chatId);
                const currentChatRoom = chatRooms.find(chatRoom => chatRoom.id === chatId);
                if(currentChatRoom) {
                    chatRoomRef.current = currentChatRoom;
                }else{
                    chatRoomRef.current = await conversationService.getById(chatId);
                }
                setMessages(response);
            }catch(e) {
                console.log(e);
            }
        }
        if(location.pathname === "/chat/create_group" && user){
            chatRoomRef.current = null;
            setTmpChatRoomName("Nhóm chat mới của " + user.username);
            setFriendNameInput("");
            setShowInfo(false);
            setCreatingGroup(true);
        } else {
            setCreatingGroup(false);
            setChosenParticipants([]);
            setUserList([]);
            if(chatId){
                localStorage.setItem("previousPath", location.pathname);
                const foundRoom = chatRooms.find(chatRoom => {
                    console.log(chatRoom.id);
                    console.log(chatId);
                    if(chatRoom.id === chatId) return chatRoom;
                });
                console.log(foundRoom);
                // currentParticipantRef.current = foundRoom.participants.find(participant => participant.participantId === user.id);
                if (foundRoom) chatRoomRef.current = foundRoom;
                fetchMessages();
            }
        }
    }, [location.pathname, user]);

    useEffect(() => {
        const fetchConversation = async () => {
            try {
                setSearchConversationResponse(await conversationService.searchConversation(searchConversationInput));
            }catch (e){
                console.log(e);
            }
        }
        fetchConversation();
    }, [searchConversationDebounce]);

    useEffect(() => {
        if (messageEndRef.current) {
            messageEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    useEffect(() => {
        if(!showUserList) setFriendNameInput("");
    }, [showUserList]);

    useEffect(() => {
        if(inViewFriend && userList){
            friendPageNumberRef.current++;
            if(!creatingGroup){
                fetchParticipantList().then(newFriendList => {
                    setUserList(prev => prev.concat(newFriendList));
                })
            }else{
                fetchFriendList().then(newFriendList => {
                    setUserList(prev => prev.concat(newFriendList));
                })
            }
        }
    }, [inViewFriend]);

    useEffect(() => {
        if(inViewFile && showedFile){
            filePageNumberRef.current++;
            fetchFile(fileTypeRef.current).then(newFile => {
                setShowedFile(prev => prev.concat(newFile));
            })
        }
    }, [inViewFile]);

    useEffect(() => {
        if(user){
            fetchFriendList().then(newFriendList => {
                if(newFriendList && newFriendList.length > 0){
                    setUserList(newFriendList);
                }
            })
        }
    }, [showUserList]);

    useEffect(() => {
        if(user){
            friendPageNumberRef.current = 0;
            if(!creatingGroup && chatRooms){
                fetchParticipantList().then(newFriendList => {
                    setUserList(newFriendList);
                })
            }else{
                fetchFriendList().then(newFriendList => {
                    const filteredList = newFriendList.filter(
                        (friend) => !chosenFriends.some((user) => user.id === friend.id)
                    );
                    setUserList(filteredList);
                })
            }
        }
    }, [participantInputDebounce]);

    useEffect(() => {
        const fetchFiles = async () => {
            if(showMedia || showApplication){
                try {
                    setShowedFile(await fetchFile());
                }catch (e){
                    console.log(e);
                }
            }
        }
        fetchFiles();
    }, [showMedia, showApplication]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (containerRef.current && !containerRef.current.contains(event.target)) {
                setShowUserList(false);
            }
        };

        if (showUserList) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showUserList]);

    const handleCreateChatRoomButton = async () => {
        try{
            if(chosenFriends.length < 2) {
                alert("Một nhóm chat mới phải có nhiều hơn 3 người!");
                return;
            }
            const formData = new FormData();
            formData.append("image", tmpChatRoomAvatar);
            const participantIds = chosenFriends.map(p => {
                return p.id;
            });
            const data = {
                name: tmpChatRoomName,
                type: "GROUP",
                creatorId: user.id,
                participantIds
            };
            const jsonBlob = new Blob([JSON.stringify(data)], {
                type: "application/json"
            });
            formData.append("data", jsonBlob);
            const response = await conversationService.create(formData);
            stompClientRef.current.subscribe(`/topic/group/${response.id}`, onMessageReceived);
            chatRoomsRef.current.push(response);
            chatRoomRef.current = response;
            for(const participant of chosenFriends){
                await handleNoticeToConversationAndUserOnCreatingConversation(participant);
            }
            navigate(`/chat/${response.id}`);
        }catch (e){
            console.log(e);
        }
    }


    const handleClickChatRoom = async (chatRoom) => {
        setBeingKicked(false);
        currentParticipantRef.current = chatRoom.participants.find(participant => participant.participantId === user.id);
        if(chatRoom.lastMessage && chatRoom.lastMessage.notRead.find(id => id === user.id)){
            await conversationService.updateLastMessageStatus(chatRoom.id, user.id);
            setChatRooms(prev => prev.map(prevChatRoom => {
                if (prevChatRoom.id === chatRoom.id && chatRoom.lastMessage) {
                    return {
                        ...chatRoom,
                        lastMessage: {
                            ...prevChatRoom.lastMessage,
                            notRead: chatRoom.participants.filter(id => id !== user.id)
                        }
                    };
                }
                return prevChatRoom;
            }));
        }
        chatRoomRef.current = chatRoom;
        if(showMedia) {
            filePageNumberRef.current = 0;
            const response = await fetchFile();
            setShowedFile(response);
        }
        if(showApplication) setShowedFile(await fetchFile());
        navigate(`/chat/${chatRoom.id}`);
    }

    const handleClickSearchChatRoom = async (chatRoom) => {
        if(!chatRoom.conversationId){
            const formData = new FormData();
            const data = {
                name: "",
                type: "PRIVATE",
                creatorId: user.id,
                participantIds: [chatRoom.userId]
            };
            const jsonBlob = new Blob([JSON.stringify(data)], {
                type: "application/json"
            });
            formData.append("data", jsonBlob);
            const created = await conversationService.create(formData);
            chatRoomRef.current = created;
            const participant = {
                id: chatRoom.userId,
                username: chatRoom.displayName
            }
            await handleNoticeToConversationAndUserOnCreatingConversation(participant);
            navigate(`/chat/${created.id}`);
        }else{
            navigate(`/chat/${chatRoom.conversationId}`);
        }
    }

    const handleNoticeToConversationAndUserOnCreatingConversation = async (participant) => {
        chatRoomRef.current.participants = await participantService.getParticipants(chatRoomRef.current.id);
        if(chatRoomRef.current.type === "GROUP") await sendMessage(user.username + " đã thêm " + participant.username + " vào nhóm.", "CONVERSATION_NOTICE");
        await sendNoticeToUser("được thêm vào nhóm", participant.id);
    }

    const handleAddParticipants = async () => {
        try {
            for(const participant of chosenParticipants){
                const response1 = await participantService.addParticipant(chatRoomRef.current.id, participant.id);
                if (response1 !== "Participant added") alert("Có lỗi xảy ra")
                else await handleNoticeToConversationAndUserOnCreatingConversation(participant);
            }
            if(!creatingGroup){
                setOption21(false);
                setChosenParticipants([]);
                friendPageNumberRef.current = 0;
            }
        }catch (e){
            console.log(e);
        }
    }

    const fetchParticipantList = async () => {
        try {
            const response = await friendService.findFriends(user.id, friendNameInput, friendPageNumberRef.current, 10);
            const res = response.filter(friend =>
                !chatRoomRef.current.participants.some(participant => participant.participantId === friend.id)
            );
            return res || [];
        }catch(e) {
            console.log(e);
        }
    }

    const fetchFriendList = async () => {
        try {
            return await friendService.findFriends(user.id, friendNameInput, friendPageNumberRef.current, 10);
        }catch (e){
            console.log(e);
        }
    }

    const fetchFile = async () => {
        try {
            return await conversationService.getConversationFiles(chatRoomRef.current.id, fileTypeRef.current, filePageNumberRef.current);
        } catch (e) {
            console.log(e);
        }
    }

    const fetchChatRooms = async () => {
        try {
            return await conversationService.getAll();
        }catch(err) {
            console.log(err);
        }
    }

    const sendNoticeToUser = useCallback(async (content, opponentId) => {
        const payload = {
            recipientId: opponentId,
            content,
            conversationId: chatRoomRef.current.id
        }
        stompClientRef.current.publish({
            destination: '/app/privateNotice',
            body: JSON.stringify(payload)
        })
    }, [])


    const onMessageReceived = useCallback(async (payload) => {
        setTimeout(async () => {
            const message = JSON.parse(payload.body);
            if(chatRoomRef.current && message.conversationId === chatRoomRef.current.id){
                if(chatRoomRef.current.lastMessage) {
                    const response = await conversationService.updateLastMessageStatus(chatRoomRef.current.id, userRef.current.id);
                    if(response !== "Status updated"){
                        alert("Có lỗi xảy ra");
                        return;
                    }
                }
                if(message.sender.id !== userRef.current.id){
                    setMessages((prev) => [...prev, message]);
                    const tmpMediaFile = message.mediaList.filter(media => media.type === "IMAGE" || media.type === "VIDEO");
                    const tmpApplicationFile = message.mediaList.filter(media => media.type === "APPLICATION");
                    if(showMediaRef.current) setShowedFile(prev => prev.concat(tmpMediaFile));
                    else if(showApplicationRef.current) setShowedFile(prev => prev.concat(tmpApplicationFile));
                }
            }
            const tmpChatRooms = await fetchChatRooms();
            if(message.content.includes("đã đổi biệt danh") && chatRoomRef.current.id === message.conversationId){
                setMessages(await messageService.getMessages(message.conversationId, userRef.current.id));
            }else if(message.content.includes("đã đổi ảnh nhóm")){
                chatRoomRef.current.avatar = tmpChatRooms.find(chatRoom => chatRoom.id === message.conversationId).avatar;
            }
            setChatRooms(tmpChatRooms);
        }, 50);
    }, [])

    const onNoticeReceived = useCallback(async (payload) => {
        const message = JSON.parse(payload.body);
        if(message.content === ("bị xóa khỏi nhóm")){
            unsubscribe(message.conversationId);
            setChatRooms(prev => prev.filter(chatRoom => chatRoom.id !== chatRoomRef.current.id));
            setBeingKicked(true);
            if(chatRoomRef.current.id === message.conversationId){
                chatRoomRef.current.participants = chatRoomRef.current.participants.filter(participant => participant.participantId !== userRef.current.id);
            }
        }else if(message.content === "được thêm vào nhóm"){
            if(message.conversationId === chatRoomRef.current.id){
                setBeingKicked(false);
            }
            stompClientRef.current.subscribe(
                `/topic/group/${message.conversationId}`, onMessageReceived
            );
            setChatRooms(await fetchChatRooms());
        }
    }, [onMessageReceived])

    const sendMessage = useCallback(async (content, type) => {
        if(content.length === 0 && mediaListRef.current.length === 0){
            alert("Hãy nhập tin nhắn hoặc gửi file!");
            return;
        }
        const tmpMediaFile = [];
        const tmpApplicationFile = [];
        let response = [];
        if(mediaListRef.current.length > 0){
            const formData = new FormData();
            for(const file of mediaListRef.current){
                formData.append('file', file);
                if(file.type.startsWith("image") || file.type.startsWith("video")){
                    tmpMediaFile.push({
                        name: file.name,
                        url: URL.createObjectURL(file),
                        size: file.size,
                        type: file.type.split("/")[0].toUpperCase()
                });
                }else if(file.type.startsWith("application")){
                    tmpApplicationFile.push({
                        name: file.name,
                        url: URL.createObjectURL(file),
                        size: file.size,
                        type: file.type.split("/")[0].toUpperCase()
                    });
                }
            }
            formData.append('conversationId', chatRoomRef.current.id);
            response = await chatService.uploadMessageFile(formData);
        }
        const payload = {
            senderId: userRef.current.id,
            content: content,
            conversationId: chatRoomRef.current.id,
            type: type,
            mediaList: response
        }
        if(chatRoomRef.current.type === 'GROUP'){
            stompClientRef.current.publish({
                destination: `/app/groupChat`,
                body: JSON.stringify(payload)
            });
        }else{
            payload.recipientId = chatRoomRef.current.participants.find(participant => participant.participantId !== userRef.current.id).participantId;
            stompClientRef.current.publish({
                destination: `/app/privateChat`,
                body: JSON.stringify(payload)
            });
        }
        if(showMediaRef.current) setShowedFile(prev => prev.concat(tmpMediaFile));
        else if(showApplicationRef.current) setShowedFile(prev => prev.concat(tmpApplicationFile));
        const lastMessage = {
            senderName: chatRoomRef.current.participants.find(participant => participant.participantId === userRef.current.id).participantName,
            senderId: userRef.current.id,
            content: content,
            notRead: chatRoomRef.current.participants.filter(id => id !== userRef.current.id),
            sentAt: new Date()
        }
        if(!chatRoomsRef.current.find(chatRoom => chatRoom.id === chatRoomRef.current.id) && !filterChoice){
            setChatRooms(prev => [chatRoomRef.current, ...prev]);
        }
        setChatRooms(prev => prev.map(chatRoom => {
            if(chatRoom.id === chatRoomRef.current.id){
                return {
                    ...chatRoom,
                    lastMessage: lastMessage
                }
            }
            return chatRoom;
        }))
        setMessageInput("");
        setMediaList([]);
        const messageId = await chatService.getLastMessageIdByConversationId(chatRoomRef.current.id, userRef.current.id);
        const messageDTO = {
            id: messageId,
            sender: userRef.current,
            content: content,
            mediaList: response,
            conversationId: chatRoomRef.current.id,
            type: type,
            sentAt: new Date()
        }
        setMessages(prev => [...prev, messageDTO]);
    }, [])

    const showFileSize = (size) => {
        const tmp = size / 1000;
        if(tmp < 1024){
            return `${tmp.toFixed(0)} KB`;
        }
        return `${(tmp / 1024).toFixed(0)} MB`;
    }

    const showFiles = useCallback((files, type) => {
        const imageVideoList = [];
        const fileList = [];
        files.forEach((file) => {
            if (file.type === "IMAGE" || file.type === "VIDEO") {
                imageVideoList.push(file);
            } else {
                fileList.push(file);
            }
        });

        return (
            <>
                {imageVideoList.length > 0 && (
                    <div className={`media-grid media-count-${imageVideoList.length}`}>
                        {imageVideoList.map((file, index) => (
                            <img src={file.url} alt="" key={index} />
                        ))}
                    </div>
                )}
                {fileList.map((file, index) => (
                    <a target="_blank" className={`file-info-message ${type}`} key={index} href={file.url} download>
                        <FontAwesomeIcon icon={faFile} style={{ fontSize: '35px' }} />
                        <div style={{ maxWidth: '230px', overflow: 'hidden' }}>
                            <p className="file-name">{file.name}</p>
                            <p className="file-size">{showFileSize(file.size)}</p>
                        </div>
                    </a>
                ))}
            </>
        );
    }, []);

    const getCurtainContent = () => {
        return(
            <div className="popup">
                {option11 && (
                    <>
                        <p className="popup-title">Đổi tên đoạn chat</p>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            padding: '15px',
                            gap: '10px'
                        }}>
                            <input
                                type="text"
                                maxLength="30"
                                value={optionInput}
                                placeholder="Nhập tên đoạn chat"
                                style={{
                                    width: '400px',
                                    height: '20px',
                                    border: '2px solid lightgray',
                                    borderRadius: '7px',
                                    fontSize: '20px',
                                    padding: '10px',
                                    outline: 'none'
                                }}
                                onChange={(e) => setOptionInput(e.target.value)}
                            />
                            <div style={{
                                display: 'flex',
                                gap: '10px',
                                alignSelf: 'flex-end'
                            }}>
                                <button className="cancel-button" onClick={() => setOption11(false)}>
                                    Hủy
                                </button>
                                <button className={`confirm-button ${optionInput === chatRoomRef.current.name && "unavailable"}`} onClick={async () => {
                                    if(optionInput !== chatRoomRef.current.name) {
                                        try {
                                            const response = await conversationService.updateName(chatRoomRef.current.id, optionInput);
                                            if(response !== "Name updated") alert("Có lỗi xảy ra");
                                            else {
                                                setOption11(false);
                                                chatRoomRef.current.name = optionInput;
                                                localStorage.setItem("chatRoom", JSON.stringify(chatRoomRef.current));
                                                await sendMessage(user.username + " đã đổi tên nhóm thành " + optionInput + ".", "CONVERSATION_NOTICE");
                                                setOptionInput("");
                                            }
                                        }catch(e) {
                                            console.log(e);
                                        }
                                    }
                                }}>
                                    Lưu
                                </button>
                            </div>
                        </div>
                    </>
                )}
                {option13 && (
                    <>
                        <p className="popup-title">Đổi biệt danh</p>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            padding: '15px',
                            gap: '10px'
                        }}>
                            {chatRoomRef.current.participants.map((participant, index) => (
                                <ParticipantListNicknameChatRoom setChatRooms={setChatRooms} user={user} stompClientRef={stompClientRef} sendMessage={sendMessage} setMessages={setMessages} optionInput={optionInput} setOptionInput={setOptionInput} participant={participant} index={index} chatRoomRef={chatRoomRef}/>
                            ))}
                            <FontAwesomeIcon icon={faX} style={{
                                fontSize: '15px',
                                padding: '10px',
                                borderRadius: '50%',
                                position: 'absolute',
                                top: '10px',
                                right: '10px',
                                cursor: 'pointer',
                                backgroundColor: 'lightgray'
                            }} onClick={() => setOption13(false)}/>
                        </div>
                    </>
                )}
                {option21 && (
                    <>
                        <p className="popup-title">Thêm thành viên</p>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            padding: '20px',
                            gap: '10px',
                            width: '500px',
                        }}>
                            <div className="search-new-participant-div">
                                <FontAwesomeIcon icon={faMagnifyingGlass}/>
                                <input
                                    type="text"
                                    value={friendNameInput}
                                    placeholder="Tìm kiếm bạn bè"
                                    style={{
                                        backgroundColor: 'inherit',
                                        outline: 'none',
                                        border: 'none',
                                        width: 'auto',
                                        fontSize: '19px',
                                    }}
                                    onChange={(e) => setFriendNameInput(e.target.value)}
                                />
                            </div>
                            {chosenParticipants.length > 0 && (
                                <div className="chosen-new-participant">
                                    {chosenParticipants.map((participant, index) => (
                                        <>
                                            <div key={index} style={{
                                                display: 'flex',
                                                flexDirection: 'column',
                                                alignItems: 'center',
                                                gap: '5px',
                                                position: 'relative',
                                                width: '85px'
                                            }}>
                                                <img src={`data:${getImageMime(participant.avatar)};base64,${participant.avatar}`} alt="" className="participant-avatar"/>
                                                <p style={{
                                                    color: 'rgb(117,117,117)',
                                                    fontSize: '14px',
                                                    maxWidth: '100%',
                                                    overflow: 'hidden',
                                                    textOverflow: 'ellipsis',
                                                    whiteSpace: 'nowrap'
                                                }}>{participant.username}</p>
                                                <FontAwesomeIcon icon={faXmark} style={{
                                                    fontSize: '12px',
                                                    padding: '5px',
                                                    borderRadius: '50%',
                                                    position: 'absolute',
                                                    top: '0',
                                                    right: '15px',
                                                    cursor: 'pointer',
                                                    backgroundColor: 'lightgray'
                                                }} onClick={() => setChosenParticipants(prev => prev.filter(p => p.id !== participant.id))}/>
                                            </div>
                                        </>
                                    ))}
                                </div>
                            )}
                            <div style={{
                                maxHeight: '400px',
                                overflowY: 'auto',
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '5px',
                            }}>
                                {userList && (userList.length > 0 && userList.map((friend, index) => (
                                    <FriendListChat index={index} setChosenParticipant={setChosenParticipants} opponent={friend} chosenParticipant={chosenParticipants} hasCheck={true}/>
                                )))}
                                <div ref={loadMoreFriendRef}></div>
                            </div>
                            <FontAwesomeIcon icon={faX} style={{
                                fontSize: '15px',
                                padding: '10px',
                                borderRadius: '50%',
                                position: 'absolute',
                                top: '10px',
                                right: '10px',
                                cursor: 'pointer',
                                backgroundColor: 'lightgray'
                            }} onClick={() => {
                                setOption21(false);
                                friendPageNumberRef.current = 1;
                            }}/>
                        </div>
                        <button className="confirm-button" style={{margin: '0px 20px 20px 20px'}} onClick={() => handleAddParticipants(chosenParticipants)}>Xác nhận</button>
                    </>
                )}
            </div>
        )
    }

    return user && (
        <div style={{
            backgroundColor: 'lightgray',
            position: 'relative'
        }}>
            {(option11 || option13 || option21) && (
                <>
                    <div className="curtain">
                    </div>
                    {getCurtainContent()}
                </>
            )}
            <div style={{
                display: 'flex',
                gap: '15px',
                margin: '0 auto',
                height: '100vh',
                paddingRight: '15px',
                maxWidth: '2000px',
            }}>
                <div className="left-content">
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between'
                    }}>
                        <h1>Đoạn chat</h1>
                        <div style={{
                            display: 'flex',
                            alignItems: 'center'
                        }}>
                            <FontAwesomeIcon icon={faHouse} className="function-button-for-flex" onClick={() => navigate('/')}/>
                            <FontAwesomeIcon icon={faPenToSquare} className="function-button-for-flex" onClick={async () => {
                                navigate("/chat/create_group");
                            }}/>
                        </div>
                    </div>
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                    }}>
                        {searchingConversation &&
                            <FontAwesomeIcon icon={faArrowLeft} className="back-icon" style={{marginLeft: '0'}} onClick={() => {
                                setSearchingConversation(false);
                                setSearchConversationInput("");}}
                            />
                        }
                        <div className="search-bounding">
                            <FontAwesomeIcon icon={faMagnifyingGlass} style={{fontSize: '20px'}}/>
                            <input
                                type="text" placeholder="Tìm kiếm trên PTIT Connect"
                                style={{
                                    width: '100%',
                                    border: 'none',
                                    fontSize: '20px',
                                    backgroundColor: 'inherit',
                                    outline: 'none'
                                }}
                                onClick={() => setSearchingConversation(true)}
                                onChange={(e) => setSearchConversationInput(e.target.value)}
                            />
                        </div>
                    </div>
                    {!searchingConversation &&
                        <div style={{
                            display: 'flex',
                            gap: '5px',
                            fontSize: '18px',
                            fontWeight: 'bold',
                            marginTop: '10px'
                        }}>
                        <p className={`filter-choice ${!filterChoice && "selected"}`} onClick={() => {
                            setFilterChoice(0);
                        }}>Tất cả</p>
                        <p className={`filter-choice ${filterChoice && "selected"}`} onClick={() => {
                            setFilterChoice(1);
                        }}>Chưa đọc</p>
                        </div>
                    }
                    <div className="chat-room-container">
                        {!searchingConversation ? (
                            <React.Fragment>
                                {creatingGroup && (
                                    <div className="chat-room">
                                        <img src={tmpChatRoomAvatar ? URL.createObjectURL(tmpChatRoomAvatar) : "/default-chat-room-avatar.png"} alt="" className="chat-room-avatar"/>
                                        <p className="chat-room-name">{tmpChatRoomName}</p>
                                        <FontAwesomeIcon icon={faXmark} style={{fontSize: '25px', marginLeft: 'auto'}} onClick={() => {
                                            const previousPath = localStorage.getItem("previousPath");
                                            if(!previousPath) navigate("/chat");
                                            else navigate(previousPath);
                                        }}/>
                                    </div>
                                )}
                                {chatRooms && chatRooms.map((data, index) => (
                                    <ChatRoomList user={user} data={data} index={index} handleClickChatRoom={handleClickChatRoom}/>
                                ))}
                            </React.Fragment>
                        ) : (
                            searchConversationResponse.map((data, index) => (
                                <ChatRoomList user={user} data={data} index={index} handleClickChatRoom={handleClickSearchChatRoom}/>
                            ))
                        )}
                    </div>
                </div>
                {(creatingGroup || chatRoomRef.current) &&
                    <div className="middle-content">
                        <div className="chat-room-header">
                            {chatRoomRef.current && (
                                <div className="chat-room-info">
                                    <div style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '10px'
                                    }}>
                                        <img src={`data:${getImageMime(chatRoomRef.current.avatar)};base64,${chatRoomRef.current.avatar}`} alt="" style={{
                                            width: '50px',
                                            height: '50px',
                                            borderRadius: '50%',
                                        }}/>
                                        <div style={{
                                            display: 'flex',
                                            flexDirection: 'column',
                                            justifyContent: 'center',
                                            gap: '8px'
                                        }}>
                                            <p style={{fontWeight : 'bold', fontSize : "19px"}}>{chatRoomRef.current.name ? chatRoomRef.current.name : chatRoomRef.current.displayName}</p>
                                            <p style={{color: "rgb(117,117,117)", fontSize: "15px", marginTop: "-5px"}}>Online 2 phút trước</p>
                                        </div>
                                    </div>
                                    <div className="function" >
                                        <FontAwesomeIcon icon={faPhone} style={{color: '#E53935'}} onClick={() => alert("Chức năng này chưa khả dụng!")}/>
                                        <FontAwesomeIcon icon={faVideo} style={{color: '#E53935'}} onClick={() => alert("Chức năng này chưa khả dụng!")}/>
                                        <FontAwesomeIcon icon={faCircleInfo} style={{color: '#E53935'}} onClick={() => setShowInfo(!showInfo)}/>
                                    </div>
                                </div>
                            )}
                            {creatingGroup && (
                                <div style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '10px'
                                }}>
                                    <p
                                        style={{
                                            fontSize: '18px',
                                            color: 'black',
                                            fontWeight: 'bold'
                                        }}
                                    >Đến: </p>
                                    {chosenFriends && chosenFriends.map((friend, index) => (
                                        <div style={{
                                            width: '130px',
                                            display: 'flex',
                                            padding: '10px',
                                            borderRadius: '20px',
                                            alignItems: 'center',
                                            gap: '5px',
                                            fontSize: '18px',
                                            backgroundColor: 'lightgray',
                                            justifyContent: 'space-between'
                                        }} key={index}>
                                            <p className="participant-name" style={{fontSize: '15px'}}>{friend.username}</p>
                                            <FontAwesomeIcon icon={faXmark} style={{color: 'black', cursor: 'pointer'}} onClick={() => {
                                                setChosenFriends(prev => prev.filter(p => p.id !== friend.id));
                                            }}/>
                                        </div>
                                    ))}
                                    <div style={{
                                        position: 'relative'
                                    }}>
                                        <input
                                            type="text"
                                            placeholder="Tìm bạn bè"
                                            value={friendNameInput}
                                            style={{
                                                padding: '10px',
                                                width: '100%',
                                                border: 'none',
                                                fontSize: '18px',
                                                backgroundColor: 'lightgray',
                                                outline: 'none',
                                                borderRadius: '20px'
                                            }}
                                            onClick={() => setShowUserList(true)}
                                            onChange={(e) => setFriendNameInput(e.target.value)}
                                        />
                                        {showUserList && userList && userList.length > 0 &&
                                            <div ref={containerRef} style={{
                                                display: 'flex',
                                                flexDirection: 'column',
                                                gap: '5px',
                                                position: 'absolute',
                                                top: '100%',
                                                left: '0',
                                                width: '100%',
                                                backgroundColor: 'white',
                                                borderRadius: '20px',
                                                zIndex: '100',
                                                padding: '10px',
                                                boxShadow: '0px 0px 10px 0px rgba(0,0,0,0.75)',
                                            }}>
                                                <h3 style={{margin: '0'}}>Tìm bạn bè</h3>
                                                {userList.map((friend, index) => (
                                                    (!chosenFriends.find(chosenFriend => chosenFriend.id === friend.id) &&
                                                        <div key={index} className={`friend-div ${creatingGroup && "small-font"}`} onClick={() => {
                                                            setChosenFriends(prev => prev.concat(friend));
                                                            setShowUserList(false);
                                                        }}>
                                                            <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt="" className="participant-avatar"/>
                                                            <p className="participant-name">{friend.username}</p>
                                                        </div>
                                                    )
                                                ))}
                                                <div ref={loadMoreFriendRef}></div>
                                            </div>
                                        }
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="chat-room-messages">
                            <div className={`spacing ${creatingGroup && "hidden"}`}></div>
                            {!creatingGroup ? messages.map((message, index) => (
                                message.type === "CONVERSATION_NOTICE" ?
                                    <p className="notice-message" key={index}>{message.content}</p> :
                                    (
                                        message.sender.id === user.id ? (
                                            <div className="sender-message" key={index}>
                                                {message.content !== "" &&
                                                    <p style={{
                                                        backgroundColor: '#E53935',
                                                        padding: '10px',
                                                        borderRadius: '25px',
                                                        color: 'white',
                                                        lineHeight: '1.3'
                                                    }}>{message.content}</p>
                                                }
                                                {showFiles(message.mediaList, "sender")}
                                            </div>
                                        ) : (
                                            <div className="recipient-message" key={index}>
                                                <div style={{
                                                    display: 'flex',
                                                    alignItems: 'flex-end',
                                                    gap: '10px'
                                                }}>
                                                    <img
                                                        src={`data:${getImageMime(message.sender.avatar)};base64,${message.sender.avatar}`}
                                                        alt="" style={{
                                                        width: '50px',
                                                        height: '50px',
                                                        objectFit: 'cover',
                                                        borderRadius: '50%',
                                                    }}/>
                                                    <div className="message-content">
                                                        <p style={{
                                                            fontSize: '15px',
                                                            color: 'rgb(128,128,128)'
                                                        }}>{message.sender.username}</p>
                                                        {message.content !== "" &&
                                                            <p style={{
                                                                backgroundColor: 'lightgray',
                                                                padding: '10px',
                                                                borderRadius: '25px',
                                                                color: 'black',
                                                                lineHeight: '1.3',
                                                                alignSelf: 'flex-start'
                                                            }}>{message.content}</p>}
                                                        {showFiles(message.mediaList, "recipient")}
                                                    </div>
                                                </div>
                                            </div>
                                        )
                                    )
                            )) : (
                                <div style={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: '20px',
                                    alignSelf: 'center',
                                    justifyContent: 'center',
                                    alignItems: 'center'
                                }}>
                                    <div style={{position: 'relative'}}>
                                        <img src={tmpChatRoomAvatar ? URL.createObjectURL(tmpChatRoomAvatar) : "/default-chat-room-avatar.png"} alt=""
                                             style={{
                                                 width: '100px',
                                                 height: '100px',
                                                 objectFit: 'cover',
                                                 borderRadius: '50%',
                                                 border: '2px solid lightgray'
                                             }}
                                        />
                                        <label htmlFor="chat-room-avatar-input">
                                            <FontAwesomeIcon icon={faCamera} className="icon-down-right-corner" style={{
                                                backgroundColor: 'lightgray',
                                                borderRadius: '50%',
                                                padding: '5px',
                                                cursor: 'pointer'
                                            }}/>
                                            <input
                                                type="file"
                                                id="chat-room-avatar-input"
                                                hidden
                                                accept="image/*"
                                                onChange={(e) => setTmpChatRoomAvatar(e.target.files[0])}
                                            />
                                        </label>
                                        {tmpChatRoomAvatar &&
                                            <FontAwesomeIcon icon={faXmark} className="icon-top-right-corner" style={{
                                                backgroundColor: 'lightgray',
                                                borderRadius: '50%',
                                                padding: '5px',
                                                cursor: 'pointer'
                                            }} onClick={() => setTmpChatRoomAvatar(null)}/>
                                        }
                                    </div>
                                    <input
                                        className="tmp-chat-room-name"
                                        maxLength={35}
                                        value={tmpChatRoomName}
                                        onChange={(e) => {
                                            setTmpChatRoomName(e.target.value)
                                        }}/>
                                    <button className="create-chat-room-button" onClick={handleCreateChatRoomButton}>
                                        Tạo nhóm chat
                                    </button>
                                </div>
                            )}
                            <div ref={messageEndRef}></div>
                        </div>
                        <div>
                            {(!beingKicked && !creatingGroup) ? (
                                <div className={`chat-room-bottom`}>
                                    <label htmlFor="file-upload">
                                        <FontAwesomeIcon icon={faCamera} style={{
                                            fontSize: '30px',
                                            cursor: 'pointer',
                                            marginBottom: '4px',
                                            color: '#E53935'
                                        }}/>
                                        <input
                                            type="file"
                                            id="file-upload"
                                            hidden
                                            multiple
                                            accept="image/*, video/*,
                                    .doc,.docx,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,
                                    .xls,.xlsx,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
                                    .pdf,application/pdf"
                                            onChange={(e) => setMediaList([...e.target.files])}
                                        />
                                    </label>
                                    <div style={{
                                        width: '90%',
                                        padding: '10px',
                                        backgroundColor: 'lightgray',
                                        borderRadius: '20px',
                                    }}>
                                        {mediaList.length > 0 &&
                                            <div className="tmp-media-list">
                                                <label htmlFor="file-upload-tmp">
                                                    <FontAwesomeIcon icon={faPlus} style={{
                                                        fontSize: '30px',
                                                        padding: '15px',
                                                        cursor: 'pointer',
                                                        backgroundColor: '#efefef',
                                                        borderRadius: '15px',
                                                        border: '1px solid #E53935',
                                                        borderStyle: 'dashed'
                                                    }}/>
                                                    <input
                                                        type="file"
                                                        id="file-upload-tmp"
                                                        hidden
                                                        multiple
                                                        accept="image/*, video/*,
                                    .doc,.docx,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,
                                    .xls,.xlsx,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
                                    .pdf,application/pdf"
                                                        onChange={(e) => setMediaList(prev => [...prev, ...e.target.files])}
                                                    />
                                                </label>
                                                {mediaList.map((file, index) => (
                                                    <div style={{
                                                        position: 'relative',
                                                        display: 'flex',
                                                        alignItems: 'center'
                                                    }} key={index}>
                                                        {file.type.includes('image') || file.type.includes('video') ? (
                                                            <img src={URL.createObjectURL(file)} alt="" style={{
                                                                width: '60px',
                                                                height: '60px',
                                                                borderRadius: '10px',
                                                                objectFit: 'cover'
                                                            }}/>
                                                        ) : (
                                                            <div className = "file-info">
                                                                <FontAwesomeIcon icon={faFile} style={{
                                                                    fontSize: '25px'
                                                                }}/>
                                                                <p className="file-name">{file.name}</p>
                                                            </div>
                                                        )}
                                                        <FontAwesomeIcon icon={faXmark} style={{
                                                            fontSize: '20px',
                                                            position: 'absolute',
                                                            top: '-5px',
                                                            right: '-5px',
                                                            cursor: 'pointer',
                                                            width: '20px',
                                                            height: '20px',
                                                            backgroundColor: 'white',
                                                            borderRadius: '50%',
                                                        }} onClick={() => setMediaList(mediaList.filter((_, i) => i !== index))} />
                                                    </div>
                                                ))}
                                            </div>
                                        }
                                        <input
                                            className="chat-room-message-input"
                                            placeholder="Nhập tin nhắn"
                                            value={messageInput}
                                            onChange={(e) => setMessageInput(e.target.value)}
                                            onKeyDown={async (e) => {
                                                if (e.key === "Enter") {
                                                    await sendMessage(messageInput, "NORMAL");
                                                    mediaListRef.current = mediaList;
                                                }
                                            }}
                                        />
                                    </div>
                                    <div className="send-icon" onClick={async () => {
                                        mediaListRef.current = mediaList;
                                        await sendMessage(messageInput, "NORMAL");
                                    }}></div>
                                </div>
                            ) : (!creatingGroup &&
                                <p style={{
                                    color: 'red',
                                    fontSize: '20px',
                                    textAlign: 'center',
                                    padding: '15px'
                                }}>Bạn không thể nhắn tin vào nhóm này</p>
                            )}
                        </div>
                    </div>
                }
                {showInfo &&
                    <div className="right-content">
                        {!option312 ? (
                            <React.Fragment>
                                <img src={`data:${getImageMime(chatRoomRef.current.avatar)};base64,${chatRoomRef.current.avatar}`} alt="" style={{
                                    width: '80px',
                                    height: '80px',
                                    borderRadius: '50%',
                                    objectFit: 'cover'
                                }}/>
                                <p style={{fontSize: '20px', fontWeight: 'bold', marginTop: '10px', marginBottom: '40px'}}>{chatRoomRef.current.name ? chatRoomRef.current.name : chatRoomRef.current.displayName}</p>
                                <div className="option">
                                    <div className = "option-label" onClick={() => setOption1(prev => !prev)}>
                                        <p>Tùy chỉnh đoạn chat</p>
                                        {!option1 ? <FontAwesomeIcon icon={faChevronDown}/> : <FontAwesomeIcon icon={faChevronUp}/>}
                                    </div>
                                    {option1 &&
                                        <div>
                                            {chatRoomRef.current.type === "GROUP" && (
                                                <React.Fragment>
                                                    <div className="option-content" onClick={() => {
                                                        if(beingKicked){
                                                            alert("Bạn không thể thực hiện chức năng này");
                                                            return;
                                                        }
                                                        setOption11(true);
                                                        setOptionInput(chatRoomRef.current.name);
                                                    }}>
                                                        <div className="option-item">
                                                            <FontAwesomeIcon icon={faPen} style={{color: '#E53935', fontSize: '25px'}}/>
                                                            <p>Đổi tên đoạn chat</p>
                                                        </div>
                                                    </div>
                                                    <label htmlFor="file-upload-avatar" className="option-content">
                                                        <div className="option-item">
                                                            <FontAwesomeIcon icon={faImage} style={{color: '#E53935', fontSize: '25px'}}/>
                                                            <p>Thay đổi ảnh</p>
                                                        </div>
                                                        <input
                                                            type="file"
                                                            id="file-upload-avatar"
                                                            hidden
                                                            accept="image/*"
                                                            onClick={(e) => {
                                                                if(beingKicked){
                                                                    e.preventDefault();
                                                                    alert("Bạn không thể thực hiện chức năng này");
                                                                }
                                                            }}
                                                            onChange={async (e) => {
                                                                const formData = new FormData();
                                                                formData.append('image', e.target.files[0]);
                                                                try {
                                                                    chatRoomRef.current.avatar = await conversationService.changeAvatar(chatRoomRef.current.id, formData);
                                                                    await sendMessage(user.username + " đã đổi ảnh nhóm.", "CONVERSATION_NOTICE");
                                                                    localStorage.setItem("chatRoom", JSON.stringify(chatRoomRef.current));
                                                                    // setChatRooms(prev => prev.map(chatRoom => {
                                                                    //     if(chatRoom.id === chatRoomRef.current.id){
                                                                    //         return {
                                                                    //             ...chatRoom,
                                                                    //             avatar: response
                                                                    //         }
                                                                    //     }
                                                                    //     return chatRoom;
                                                                    // }));
                                                                }catch (e) {
                                                                    console.log(e);
                                                                }
                                                            }}
                                                        />
                                                    </label>
                                                </React.Fragment>
                                            )}
                                            <div className="option-content" onClick={() => {
                                                if(beingKicked){
                                                    alert("Bạn không thể thực hiện chức năng này");
                                                    return;
                                                }
                                                setOption13(true);
                                                setOptionInput("");
                                            }}>
                                                <div className="option-item">
                                                    <div className="change-nickname-icon"></div>
                                                    <p>Chỉnh sửa biệt danh</p>
                                                </div>
                                            </div>
                                        </div>
                                    }
                                </div>
                                <div className="option">
                                    <div className = "option-label"  onClick={() => setOption2(prev => !prev)}>
                                        <p>Thành viên trong đoạn chat</p>
                                        {!option2 ? <FontAwesomeIcon icon={faChevronDown}/> : <FontAwesomeIcon icon={faChevronUp}/>}
                                    </div>
                                    {option2 &&
                                        <div style={{
                                            display: 'flex',
                                            flexDirection: 'column',
                                            gap: '10px',
                                            padding: '5px 10px 5px 10px'
                                        }}>
                                            {chatRoomRef.current.participants.map((participant, index) =>
                                                <ParticipantListChatRoom sendNoticeToUser={sendNoticeToUser} sendMessage={sendMessage} chatRoomRef={chatRoomRef} participant={participant} index={index} currentUser={currentParticipantRef.current}/>
                                            )}
                                            {chatRoomRef.current.type === "GROUP" && (
                                            <div className="option-item" onClick={() => {
                                                if(beingKicked){
                                                    alert("Bạn không thể thực hiện chức năng này");
                                                    return;
                                                }
                                                setOption21(true);
                                                friendPageNumberRef.current = 0;
                                                fetchParticipantList().then(res => {
                                                    setUserList(res);
                                                })
                                            }}>
                                                <FontAwesomeIcon icon={faUserPlus} style={{fontSize: '25px', color: '#E53935'}}/>
                                                <p>Thêm người</p>
                                            </div>
                                            )}
                                        </div>
                                    }
                                </div>
                                <div className="option">
                                    <div className = "option-label" onClick={() => setOption3(prev => !prev)}>
                                        <p>File phương tiện & file</p>
                                        {!option3 ? <FontAwesomeIcon icon={faChevronDown}/> : <FontAwesomeIcon icon={faChevronUp}/>}
                                    </div>
                                    {option3 &&
                                        <div>
                                            <div className="option-content" onClick={() => {
                                                showMediaRef.current = true;
                                                filePageNumberRef.current = 0;
                                                fileTypeRef.current = ["IMAGE", "VIDEO"];
                                                setOption312(true);
                                                setShowMedia(true);
                                                setShowApplication(false);
                                            }}>
                                                <div className="option-item">
                                                    <FontAwesomeIcon icon={faFileImage} style={{color: '#E53935', fontSize: '25px'}}/>
                                                    <p>File phương tiện</p>
                                                </div>
                                            </div>
                                            <div className="option-content" onClick={() => {
                                                showApplicationRef.current = true;
                                                filePageNumberRef.current = 0;
                                                fileTypeRef.current = ["APPLICATION"];
                                                setOption312(true);
                                                setShowApplication(true);
                                                setShowMedia(false);
                                            }}>
                                                <div className="option-item">
                                                    <FontAwesomeIcon icon={faFileLines} style={{color: '#E53935', fontSize: '25px'}}/>
                                                    <p>File</p>
                                                </div>
                                            </div>
                                        </div>
                                    }
                                </div>
                            </React.Fragment>
                        ) : (
                            <React.Fragment>
                                <div style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    width: '100%',
                                    marginTop: '-20px'
                                }}>
                                    <FontAwesomeIcon icon={faChevronLeft} className="back-icon"
                                    onClick={() => setOption312(false)}
                                    />
                                    <h3 style={{
                                        padding: '10px',
                                    }}>File phương tiện và file</h3>
                                </div>
                                <div style={{
                                    display: 'flex',
                                    marginRight: 'auto',
                                }}>
                                    <p className={`file-category ${showMedia && "selected"}`} onClick={() => {
                                        if(!showMedia){
                                            setShowedFile([]);
                                            showMediaRef.current = true;
                                            filePageNumberRef.current = 0;
                                            fileTypeRef.current = ["IMAGE", "VIDEO"];
                                            setShowMedia(true);
                                            setShowApplication(false);
                                        }
                                    }}>File phương tiện</p>
                                    <p className={`file-category ${showApplication && "selected"}`} onClick={() => {
                                        if(!showApplication){
                                            setShowedFile([]);
                                            showApplicationRef.current = true;
                                            filePageNumberRef.current = 0;
                                            fileTypeRef.current = ["APPLICATION"];
                                            setShowApplication(true);
                                            setShowMedia(false);
                                        }
                                    }}>File</p>
                                </div>
                                {showMedia && (
                                    <div className="show-media-grid">
                                        {showedFile.map((file, index) => (
                                            <img src={file.url} alt="" key={index}/>
                                        ))}
                                        <div ref={loadMoreFileRef}></div>
                                    </div>
                                )}
                                {showApplication && (
                                    <div style={{
                                        display: 'flex',
                                        flexDirection: 'column',
                                        gap: '10px',
                                        padding: '5px 10px 5px 10px',
                                        width: '100%'
                                    }}>
                                        {showedFile.map((file, index) => (
                                            <a target="_blank" className="file-container" key={index} href={file.url} download>
                                                <FontAwesomeIcon icon={faFileLines} style={{
                                                    fontSize: '25px',
                                                    padding: '15px',
                                                    backgroundColor: 'lightgray',
                                                    borderRadius: '10px',
                                                    marginRight: 'auto'
                                                }}/>
                                                <div style={{
                                                    maxWidth: '300px',
                                                }}>
                                                    <p className="show-file-name">{file.name}</p>
                                                    <p>{showFileSize(file.size)}</p>
                                                </div>
                                            </a>
                                        ))}
                                        <div ref={loadMoreFileRef}></div>
                                    </div>
                                )}
                            </React.Fragment>
                        )}
                    </div>
                }
            </div>
        </div>
    );
};

export default Chat;