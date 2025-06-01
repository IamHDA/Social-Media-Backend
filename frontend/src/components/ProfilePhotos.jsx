import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronDown, faTrashCan} from "@fortawesome/free-solid-svg-icons";

const ProfilePhotos = () => {
    const [showDropdown, setShowDropdown] = useState(false);
    const [selectedSort, setSelectedSort] = useState({
        name: "Ảnh mới nhất",
        value: "newest"
    });
    const option = [
        {
            name: "Ảnh mới nhất",
            value: "newest"
        },{
            name: "Ảnh cũ nhất",
            value: "oldest"
        },{
            name: "Ảnh nhiều tương tác nhất",
            value: "most-interaction"
        }
    ]

    useEffect(() => {

    })

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
                alignItems: 'center',
                justifyContent: 'space-between'
            }}>
                <h2 style={{
                    margin: 0
                }}>Ảnh</h2>
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    gap: '10px',
                    backgroundColor: '#e6e6e6',
                    borderRadius: '10px',
                    padding: '10px 15px',
                    fontSize: '18px',
                    cursor: 'pointer',
                    position: 'relative',
                    width: '210px'
                }} onClick={() => setShowDropdown(prev => !prev)}>
                    <p>Ảnh mới nhất</p>
                    <FontAwesomeIcon icon={faChevronDown} fontSize="16px"/>
                    {showDropdown && (
                        <div style={{
                            position: 'absolute',
                            top: '100%',
                            left: '0px',
                            right: '0px',
                            backgroundColor: '#e6e6e6',
                            borderBottomLeftRadius: '10px',
                            borderBottomRightRadius: '10px',
                            padding: '15px',
                            marginTop: '-10px',
                        }}>
                            {option.map((option, index) => (
                                <div key={index} style={{
                                    padding: '10px 0px',
                                    cursor: 'pointer',
                                    fontSize: '18px',
                                }} onClick={() => {
                                    setSelectedSort(option);
                                    setShowDropdown(false);
                                }}>
                                    <p>{option.name}</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <div className="profile-photo-grid">
                <div style={{
                    position: 'relative'
                }}>
                    <img src="/public/avartar-anime-39.jpg" alt=""/>
                    <FontAwesomeIcon icon={faTrashCan} className="icon-top-right-corner" fontSize="18px" style={{
                        padding: '7px',
                        backgroundColor: 'white',
                        borderRadius: '50%',
                        cursor: 'pointer'
                    }}/>
                </div>
            </div>
        </div>
    );
};

export default ProfilePhotos;