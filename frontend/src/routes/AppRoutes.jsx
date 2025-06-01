import { Routes, Route } from "react-router-dom";
import {
    Login,
    Chat,
    HomePage,
    FriendPage,
    Register,
    ProfilePage,
} from "../pages";


const AppRoutes = () => {
  return (
    <Routes>
        <Route path="/friend" element={<FriendPage />} />
        <Route path="/friend/request" element={<FriendPage />}/>
        <Route path="/friend/list" element={<FriendPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/chat/:chatId?" element={<Chat />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/chat/create_group" element={<Chat />} />
        <Route path="/profile/:userId" element={<ProfilePage />} />
        <Route path="/profile/:userId/photo" element={<ProfilePage />} />
        <Route path="/profile/:userId/video" element={<ProfilePage />} />
        <Route path="/profile/:userId/friend" element={<ProfilePage />} />
        <Route path="/register" element={<Register />} />
    </Routes>
  );
};

export default AppRoutes;
