import React, { useContext, useEffect, useRef, useState } from 'react';
import { getProfile } from "@/APIs/user.js";
import {useLocation, useParams} from "react-router-dom";
import ProfileHeader from "@/components/ProfileHeader.jsx";
import { Header } from "@/components/index.js";
import '../styles/ProfilePage.css';
import AuthContext from "@/context/AuthContext.jsx";
import ProfilePost from "@/components/ProfilePost.jsx";
import CurtainContext from "@/context/CurtainContext.jsx";
import { getPostByUserId} from "@/APIs/post.js";
import ProfilePhotos from "@/components/ProfilePhotos.jsx";
import ProfileFriend from "@/components/ProfileFriend.jsx";

const ProfilePage = () => {
    const { user } = useContext(AuthContext);
    const { showCurtain } = useContext(CurtainContext);
    const userId = useParams();
    const location = useLocation();
    const [directParam, setDirectParam] = useState(false);
    const [posts, setPosts] = useState([]);
    const [userInfo, setUserInfo] = useState(null);
    const [userFriend, setUserFriend] = useState([]);
    const [userImages, setUserImages] = useState([]);
    const [selectedChoice, setSelectedChoice] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const pathName = location.pathname;
        if(pathName.includes("photo")) setSelectedChoice(2);
        else if(pathName.includes("video")) setSelectedChoice(3);
        else if(pathName.includes("friend")) setSelectedChoice(1);
        else setSelectedChoice(0);
    }, [location]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                setDirectParam(Number(user.id) === Number(userId.userId));
                setPosts(await getPostByUserId(userId.userId, 1));
                const response = await getProfile(userId.userId);
                const userInfo = {
                    id: response.id,
                    username: response.username,
                    avatar: response.avatar,
                    background: response.backgroundImage,
                    bio: response.bio,
                    numberOfFriends: response.numberOfFriends
                };
                setUserInfo(userInfo);
                setUserFriend(response.friends);
                setUserImages(response.postedImages);
            } catch (error) {
                console.error("Lỗi khi lấy profile:", error);
            }finally {
                setLoading(false);
            }
        };
        if(user) fetchData();
    }, [user]);

    return !loading && (
        <div>
            <Header />
            {showCurtain && <div className="curtain-background"></div>}
            <div className="center-body" style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '20px'
            }}>
                <ProfileHeader selectedChoice={selectedChoice} isMine={directParam} userInfo={userInfo} setUserInfo={setUserInfo} setSelectedChoice={setSelectedChoice}/>
                {selectedChoice === 0 && <ProfilePost posts={posts} postedImages={userImages} friendList={userFriend} userInfo={userInfo} setPosts={setPosts}/>}
                {selectedChoice === 2 && <ProfilePhotos/>}
                {selectedChoice === 1 && <ProfileFriend friendList={userFriend} setFriendList={setUserFriend}/>}
            </div>
        </div>
    );
};

export default ProfilePage;
