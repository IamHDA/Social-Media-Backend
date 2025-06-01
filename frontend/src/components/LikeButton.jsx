import {useState, useRef, useEffect} from 'react';
import React from 'react';
import * as reactionService from "../APIs/reactions.js";

const LikeButton = ({reactions, currentReaction, setPosts, postId, type}) => {
    const [hovered, setHovered] = useState(false);
    const timerRef = useRef(null);
    const [postReaction, setPostReaction] = useState(currentReaction);

    useEffect(() => {
        if(currentReaction){
            setPostReaction(reactions.find(reaction => reaction.name === currentReaction.emotion));
        }
    }, [currentReaction])

    const handleLike = async (emoji) => {
        try {
            if(currentReaction){
                const response = await reactionService.changeReaction(currentReaction.id, emoji)
                const emotions = await(reactionService.getPostEmotions(postId));
                if(response === "Reaction changed") {
                    setPosts(prev => prev.map(post => {
                        if(post.id === postId) {
                            return {
                                ...post,
                                currentUserReaction: {
                                    ...post.currentUserReaction,
                                    emotion: emoji
                                },
                                reactionSummary: {
                                    emotions: emotions,
                                    total: post.reactionSummary.total
                                }
                            }
                        }
                        return post;
                    }))
                }
            }else{
                const data = {
                    id: postId,
                    emotion: emoji,
                    type: type
                }
                const response = await reactionService.sendReaction(data);
                const emotions = await(reactionService.getPostEmotions(postId));
                setPosts(prev => prev.map(post => {
                    if(post.id === postId) {
                        return {
                            ...post,
                            currentUserReaction: {
                                id: response,
                                emotion: emoji,
                            }
                            ,
                            reactionSummary: {
                                emotions: emotions,
                                total: post.reactionSummary.total + 1
                            }
                        }
                    }
                    return post;
                }))
                console.log("alo2");
            }
        }catch (e){
            console.log(e);
        }
        setHovered(false);
    }

    const handleUnlike = async () => {
        try {
            const response = await reactionService.deleteReaction(currentReaction.id);
            const emotions = await(reactionService.getPostEmotions(postId));
            if(response === "Reaction deleted") {
                setPosts(prev => prev.map(post => {
                    if(post.id === postId) {
                        return {
                            ...post,
                            currentUserReaction: null
                            ,
                            reactionSummary: {
                                emotions: emotions,
                                total: post.reactionSummary.total - 1
                            }
                        }
                    }
                    return post;
                }))
            }
            setPostReaction(null);
        }catch (e){
            console.log(e);
        }
    }

    const handleMouseEnter = () => {
        timerRef.current = setTimeout(() => {
            setHovered(true);
            // Xử lý thêm ở đây khi hover đủ thời gian
        }, 1000); // hover 1000ms (1s) mới kích hoạt
    };

    const handleMouseLeave = () => {
        clearTimeout(timerRef.current);
        setHovered(false);
    };

    return (
        <div
            className="function-item"
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onClick={() => {
                if(currentReaction) handleUnlike();
                else handleLike("LIKE");
            }}
        >
            {hovered &&
                <div className="emoji-container">
                    {reactions.map((reaction, index) => (
                        <img src={reaction.value} alt="" key={index} className="emoji" style={{
                            width: '40px',
                            height: '40px'
                        }} onClick={(e) => {
                            console.log(reaction.name);
                            e.stopPropagation();
                            handleLike(reaction.name);
                        }}/>
                    ))}
                </div>
            }
            {postReaction ?
                <img src={postReaction.value} alt="" className="emoji" /> :
                <img src="/public/reactions/border-like.png" alt="" className="emoji"/>
            }
            <p>Thích</p>
        </div>
    );
};

export default LikeButton;
