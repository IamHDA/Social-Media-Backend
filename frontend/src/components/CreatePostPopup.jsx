import React, {useContext, useEffect, useRef, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faCaretDown,
    faCaretUp, faChevronLeft,
    faEarthAsia,
    faFileImage,
    faUserGroup,
    faXmark
} from "@fortawesome/free-solid-svg-icons";
import {getFileFromUrl, getImageMime} from "@/utils/format.js";
import {createNewPost, sharePost} from "@/APIs/post.js";
import AuthContext from "@/context/AuthContext.jsx";
import CurtainContext from "@/context/CurtainContext.jsx";

const CreatePostPopup = ({opponent, setPosts, setShowCurtain}) => {
    const { user } = useContext(AuthContext);
    const {createPost, setCreatePost} = useContext(CurtainContext);
    const [showBackground, setShowBackground] = useState(false);
    const [isChosing, setIsChosing] = useState(false);
    const optionNumberRef = useRef(null);
    const [isPublic, setIsPublic] = useState(true);
    const [postMedia, setPostMedia] = useState([]);
    const [text, setText] = useState("");
    const [selectedBackground, setSelectedBackground] = useState(null);
    const backgroundColors = [
        "/public/post-background/white.jpg",
        "/public/post-background/blue.jpg",
        "/public/post-background/browne.jpg",
        "/public/post-background/gray.jpg",
        "/public/post-background/orange.jpg",
        "/public/post-background/pink.jpg",
        "/public/post-background/red.jpg",
        "/public/post-background/red-lighter.jpg",
        "/public/post-background/light-blue.jpg",
    ]

    useEffect(() => {
        if(text.length > 150){
            setSelectedBackground(null);
            setShowBackground(false);
        }
    }, [text])

    const handleSubmit = async () => {
        try {
            const formData = new FormData()
            let data = {
                content: text,
                privacy: isPublic ? "PUBLIC" : "PRIVATE",
                wallId: opponent.id
            }
            formData.append('data', JSON.stringify(data));
            if(selectedBackground){
                const backgroundImage = await getFileFromUrl(selectedBackground);
                formData.append('postBackground', backgroundImage);
            }
            if(postMedia.length > 0){
                for(let i = 0; i < postMedia.length; i++){
                    const file = postMedia[i];
                    formData.append('files', file);
                }
            }
            let response = null;
            if(createPost) response = await createNewPost(formData);
            else {
                data = {
                    content: text,
                    privacy: isPublic ? "PUBLIC" : "PRIVATE"
                }
                response = await sharePost(localStorage.getItem('postId'), data);
            }
            if(response !== null){
                console.log(response);
                setPosts(prev => [response, ...prev]);
                alert("Đăng thành công");
                setShowCurtain(false);
                setText("");
                setPostMedia([]);
                setSelectedBackground(null);
                setShowBackground(false);
                optionNumberRef.current = null;
                setIsChosing(false);
                setIsPublic(false);
            }
        }catch (e){
            console.log(e);
        }
    }

    return (
        <div className="popup">
            <p className="popup-title">{createPost ? "Tạo bài viết" : "Chia sẻ"}</p>
            <FontAwesomeIcon icon={faXmark} className="popup-close-icon" onClick={() => {
                setShowCurtain(false);
                setCreatePost(false);
            }} fontSize="20px"/>
            <div className="create-body">
                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '10px',
                    padding: '0px 15px 0px 15px',
                }}>
                    <img src={`data:${getImageMime(user.avatar)};base64,${user.avatar}`} alt="" className="post-creator-avatar"/>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '5px'
                    }}>
                        <p className="post-creator-name">{user.username}</p>
                        <div className="post-type" onClick={() => setIsChosing(!isChosing)}>
                            {isPublic ? <FontAwesomeIcon icon={faEarthAsia} fontSize="15"/> : <FontAwesomeIcon icon={faUserGroup} fontSize="15"/>}
                            {isPublic ? <p className="type-text">Công khai</p> : <p className="type-text">Bạn bè</p>}
                            {!isChosing ? <FontAwesomeIcon icon={faCaretDown} fontSize="15"/> : <FontAwesomeIcon icon={faCaretUp} fontSize="15"/>}
                            {isChosing &&
                                <div className="type-dropdown">
                                    <div className="type-dropdown-item" onClick={(e) => {
                                        e.stopPropagation();
                                        setIsPublic(true);
                                        setIsChosing(false);
                                    }}>
                                        <FontAwesomeIcon icon={faEarthAsia} fontSize="15"/>
                                        <p className="type-text">Công khai</p>
                                    </div>
                                    <div className="type-dropdown-item" onClick={(e) => {
                                        e.stopPropagation();
                                        setIsPublic(false);
                                        setIsChosing(false);
                                    }}>
                                        <FontAwesomeIcon icon={faUserGroup} fontSize="15"/>
                                        <p className="type-text">Bạn bè</p>
                                    </div>
                                </div>
                            }
                        </div>
                    </div>
                </div>
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    width: '600px'
                }}>
                    <textarea
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                        placeholder={createPost ? (user.id === opponent.id ? "Bạn đang nghĩ gì?" : "Hãy viết gì đó cho " + opponent.username) : "Hãy nói gì đó về nội dung này..."}
                        style={{
                            resize: 'none',
                            border: 'none',
                            outline: 'none',
                            textAlign: selectedBackground && 'center',
                            paddingTop: selectedBackground && '80px',
                            color: selectedBackground ? 'white' : 'black',
                            fontSize: !selectedBackground ? '19px' : '22px',
                            fontWeight: selectedBackground && 'bold',
                            backgroundImage: `url(${selectedBackground})`,
                            height: createPost ? (postMedia.length > 0 ? '80px' : '150px') : '40px',
                            paddingLeft: '15px',
                            paddingRight: '15px'
                        }}
                    />
                    {postMedia.length > 0 && (
                        <div className={`post-media-grid post-media-count-${postMedia.length}`}>
                            {postMedia.length <= 6 ? (
                                postMedia.map((file, index) => (
                                    <img
                                        key={index}
                                        src={URL.createObjectURL(file)}
                                        alt=""
                                    />
                                ))
                            ) : (
                                <React.Fragment>
                                    {postMedia.slice(0, 5).map((file, index) => (
                                        <img
                                            key={index}
                                            src={URL.createObjectURL(file)}
                                            alt="   "
                                        />
                                    ))}
                                    <div
                                        key="more"
                                        style={{
                                            display: 'flex',
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            height: '150px',
                                            width: '186px',
                                            backgroundColor: 'lightgray',
                                            fontSize: '20px',
                                            fontWeight: 'bold',
                                        }}
                                    >
                                        +{postMedia.length - 5}
                                    </div>
                                </React.Fragment>
                            )}
                            <div style={{
                                position: 'absolute',
                                right: '55px',
                                top: '5px',
                                display: 'flex',
                                gap: '10px',
                                alignItems: 'center'
                            }}>
                                <label htmlFor="add-media">
                                    <div style={{
                                        backgroundColor: 'white',
                                        borderRadius: '10px',
                                        padding: '10px',
                                        display: 'flex',
                                        gap: '10px',
                                        cursor: 'pointer'
                                    }}>
                                        <FontAwesomeIcon icon={faFileImage} />
                                        Thêm ảnh
                                    </div>
                                    <input
                                        type="file"
                                        hidden
                                        id="add-media"
                                        accept="image/*, video/*"
                                        multiple
                                        onChange={(e) => setPostMedia([...e.target.files, ...postMedia])}
                                    />
                                </label>
                                <FontAwesomeIcon icon={faXmark} style={{
                                    padding: '8px 10px 8px 10px',
                                    backgroundColor: 'lightgray',
                                    borderRadius: '50%',
                                    cursor: 'pointer'
                                }} onClick={() => setPostMedia([])}/>
                            </div>
                        </div>
                    )}
                </div>
                {createPost &&
                    <div style={{
                        display: 'flex',
                        flex: 1,
                        alignItems: 'center',
                        gap: '10px',
                        paddingLeft: '15px',
                        marginTop: selectedBackground && '-11px',
                        backgroundImage: selectedBackground && `url(${selectedBackground})`
                    }}>
                        {postMedia.length === 0 &&
                            <label htmlFor="post-media-input">
                                <FontAwesomeIcon icon={faFileImage} fontSize="25px" style={{
                                    padding: '8px 10px 8px 10px',
                                    borderRadius: '50%',
                                    backgroundColor: 'lightgray',
                                    cursor: selectedBackground ? 'not-allowed' : 'pointer',
                                    display: showBackground && 'none'
                                }}/>
                                <input
                                    type="file"
                                    hidden
                                    accept="image/*, video/*"
                                    id="post-media-input"
                                    multiple
                                    onChange={(e) => setPostMedia([...e.target.files])}
                                />
                            </label>
                        }
                        <div className={`show-background-color ${showBackground && 'active'}`} style={{
                            display: postMedia.length > 0 && 'none'
                        }} onClick={() => {
                            if(text.length > 150) return;
                            setShowBackground(!showBackground)
                        }}>
                            {!showBackground && <p style={{
                                fontSize: '18px',
                                fontWeight: 'bold',
                                color: 'white'
                            }} >
                                Aa
                            </p>}
                            {showBackground && <FontAwesomeIcon icon={faChevronLeft} fontSize="20px"/>}
                        </div>
                        {showBackground && backgroundColors.map((color, index) => (
                            <img className={`background-option ${optionNumberRef.current === index && 'chosen'} ${optionNumberRef.current === 0 && index === 0 && 'white'}`} key={index} src={color} alt="" onClick={() => {
                                optionNumberRef.current = index;
                                if(index !== 0) setSelectedBackground(color);
                                else setSelectedBackground(null);
                            }}/>
                        ))}
                    </div>
                }
            </div>
            <div style={{
                padding: '0px 15px 15px 15px',
                flex: '1'
            }}>
                <button className="confirm-button"
                    style={{
                        width: '100%',
                        backgroundColor: (text.length === 0 && postMedia.length === 0) && 'lightgray',
                        cursor: (text.length === 0 && postMedia.length === 0) && 'not-allowed'
                    }}
                    onClick={handleSubmit}
                >{createPost ? "Đăng" : "Chia sẻ ngay"}</button>
            </div>
        </div>
    );
};

export default CreatePostPopup;