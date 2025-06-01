import React from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEllipsis, faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";
import {getImageMime} from "@/utils/format.js";

const ProfileFriend = ({friendList, setFriendList}) => {
    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            marginLeft: '30px',
            marginRight: '30px',
            borderRadius: '10px',
            padding: '20px',
            backgroundColor: 'white'
        }}>
            <div style={{
                display: 'flex',
                textAlign: 'center',
                justifyContent: 'space-between',
                marginBottom: '20px',
            }}>
                <h2 style={{
                    margin: 0
                }}>Bạn bè</h2>
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '10px',
                    backgroundColor: '#e6e6e6',
                    borderRadius: '20px',
                    padding: '10px 15px',
                    cursor: 'pointer'
                }}>
                    <FontAwesomeIcon icon={faMagnifyingGlass} fontSize="20px" color="#a1a1a1"/>
                    <input
                        type="text"
                        placeholder="Tìm kiếm bạn bè"
                        style={{
                        border: 'none',
                        outline: 'none',
                        backgroundColor: 'transparent',
                        fontSize: '18px',
                        width: '100%'
                    }}/>
                </div>
            </div>
            <div className="profile-friend-grid">
                {friendList.length > 0 && friendList.map((friend, index) => (
                    <div key={index} style={{
                        display: 'flex',
                        alignItems: 'center',
                        padding: '10px',
                        border: '1px solid #E5E5E5',
                        borderRadius: '10px',
                        justifyContent: 'space-between',
                    }}>
                        <div style={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: '10px'
                        }}>
                            <img src={`data:${getImageMime(friend.avatar)};base64,${friend.avatar}`} alt="" style={{
                                width: '70px',
                                height: '70px',
                                borderRadius: '10px',
                                objectFit: 'cover'
                            }}/>
                            <p style={{
                                fontSize: '20px',
                                fontWeight: 'bold'
                            }}>
                                {friend.username}
                            </p>
                        </div>
                        <FontAwesomeIcon icon={faEllipsis} fontSize="30px"/>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProfileFriend;