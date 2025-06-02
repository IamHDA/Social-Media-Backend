import React, {createContext, useState} from 'react';

export const CurtainContext = createContext();

export const CurtainProvider = ({children}) => {
    const [showCurtain, setShowCurtain] = useState(false);
    const [createPost, setCreatePost] = useState(false);

    return (
        <CurtainContext.Provider value={{showCurtain, setShowCurtain, createPost, setCreatePost }}>
            {children}
        </CurtainContext.Provider>
    );
};

export default CurtainContext;