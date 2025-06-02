import React, {useContext, useEffect, useRef, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronDown, faTrashCan} from "@fortawesome/free-solid-svg-icons";
import {getAllImages} from "@/APIs/user.js";
import AuthContext from "@/context/AuthContext.jsx";
import {useInView} from "react-intersection-observer";

const ProfilePhotos = () => {
    const {user} = useContext(AuthContext);
    const [showDropdown, setShowDropdown] = useState(false);
    const pageNumberRef = useRef(0);
    const [images, setImages] = useState([]);
    const [selectedSort, setSelectedSort] = useState({
        name: "Ảnh mới nhất",
        value: "desc"
    });
    const {ref, inView} = useInView();
    const option = [
        {
            name: "Ảnh mới nhất",
            value: "desc"
        },{
            name: "Ảnh cũ nhất",
            value: "asc"
        }
    ]

    useEffect(() => {
        if(inView) {
            pageNumberRef.current = pageNumberRef.current + 1;
            getAllImages(user.id, pageNumberRef.current, 15, selectedSort.value).then((res) => {
                if(res.length > 0) {
                    setImages(prev => [...prev, res]);
                }
            })
        }
    }, [inView])

    useEffect(() => {
        if(user) {
            console.log(selectedSort.value);
            getAllImages(user.id, pageNumberRef.current, 15, selectedSort.value).then((res) => {
                setImages(res);
            })
        }
    }, [selectedSort]);

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
                    <p>{selectedSort.name}</p>
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
                                    pageNumberRef.current = 0;
                                    setShowDropdown(false);
                                    setSelectedSort(option);
                                }}>
                                    <p>{option.name}</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <div className="profile-photo-grid">
                {images.length > 0 && images.map((image, index) => (
                    <div style={{
                        position: 'relative'
                    }} key={index}>
                        <img src={image.url} alt=""/>
                        <FontAwesomeIcon icon={faTrashCan} className="icon-top-right-corner" fontSize="18px" style={{
                            padding: '7px',
                            backgroundColor: 'white',
                            borderRadius: '50%',
                            cursor: 'pointer'
                        }}/>
                    </div>
                ))}
                <div ref={ref}></div>
            </div>
        </div>
    );
};

export default ProfilePhotos;