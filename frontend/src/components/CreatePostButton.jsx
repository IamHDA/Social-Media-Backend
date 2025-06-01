import React, {useContext} from 'react';
import AuthContext from "@/context/AuthContext.jsx";
import '../styles/CreatePost.css'
import {getImageMime} from "@/utils/format.js";
import CurtainContext from "@/context/CurtainContext.jsx";
import '../styles/Post.css'

const CreatePostButton = ({opponent}) => {
    const {user} = useContext(AuthContext);
    const {setShowCurtain, setCreatePost} = useContext(CurtainContext);

    return (
        <div style={{
            display: 'flex',
            gap: '20px',
            padding: '10px 15px 10px 15px',
            borderRadius: '10px',
            backgroundColor: 'white'
        }}>
            <img src={`data:${getImageMime(user.avatar)};base64,${user.avatar}`} className="post-creator-avatar" alt=""/>
            <button className="create-post-button" onClick={() => {
                setShowCurtain(true);
                setCreatePost(true);
            }}>{user.id === opponent.id ? "Bạn đang nghĩ gì?" : "Hãy viết gì đó cho " + opponent.username}</button>
        </div>
    );
};

export default CreatePostButton;