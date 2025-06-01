import React, {useEffect, useState} from 'react';
import {getImageMime} from "../utils/format.js";
import {faCheck} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const FriendListChat = ({setTmpChatRoomName,setChosenParticipant, opponent, index, chosenParticipant, hasCheck, setShowUserList}) => {
    const [chosen, setChosen] = useState(false);

    useEffect(() => {
        if(chosen && opponent) {
            setChosenParticipant(prev => prev.concat(opponent));
            if(!hasCheck){
                setShowUserList(false);
                setTmpChatRoomName(prev => prev.concat(opponent.username));
            }

        }
        else if(chosen === false && setChosenParticipant) setChosenParticipant(prev => prev.filter(participant => participant.id !== opponent.id));
    }, [chosen]);

    useEffect(() => {
        if(hasCheck === true && chosenParticipant && !chosenParticipant.find(participant => participant.id === opponent.id)) setChosen(false);
    }, [chosenParticipant]);

    return (
        <div key={index} className={`friend-div ${!hasCheck && "small-font"}`} onClick={() => {
            setChosen(prev => !prev);
        }}>
            <img src={`data:${getImageMime(opponent.avatar)};base64,${opponent.avatar}`} alt="" className="participant-avatar"/>
            <p className="participant-name">{opponent.username}</p>
            {hasCheck && <FontAwesomeIcon icon={faCheck} className={`check-icon ${chosen ? 'checked' : ''}`}/>}
        </div>
    );
};

export default FriendListChat;