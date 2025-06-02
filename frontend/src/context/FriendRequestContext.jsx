import React, {createContext, useContext} from 'react';
import * as friendService from "@/APIs/friend.js";
import AuthContext from "@/context/AuthContext.jsx";

export const FriendRequestContext = createContext();

export const FriendRequestProvider = ({children}) => {
    const {user} = useContext(AuthContext);

    const acceptFriendRequest = async (friend, friendRequests, setFriendRequests) => {
        try {
            const response = await friendService.acceptFriendRequest(friend.userId);
            if(response === "New friend request accepted"){
                const tmpFriend = {
                    ...friend,
                    status: "accepted"
                }
                const tmpRequestList = friendRequests.map(tmp => {
                        if(tmp.userId === friend.userId) return tmpFriend;
                        return tmp;
                    }
                );
                setFriendRequests(tmpRequestList);
            }
        }catch (e){
            console.log(e);
        }
    }

    const declineFriendRequest = async (friend, friendRequests, setFriendRequests) => {
        try {
            const response = await friendService.declineFriendRequest(friend.userId, user.id);
            if(response === "Friend request deleted"){
                const tmpFriend = {
                    ...friend,
                    status: "declined"
                }
                const tmpRequestList = friendRequests.map(tmp => {
                        if(tmp.userId === friend.userId) return tmpFriend;
                        return tmp;
                    }
                );
                setFriendRequests(tmpRequestList);
            }
        }catch (e){
            console.log(e);
        }
    }

    return (
        <FriendRequestContext.Provider value={{acceptFriendRequest, declineFriendRequest}}>
            {children}
        </FriendRequestContext.Provider>
    );
};

export default FriendRequestContext;