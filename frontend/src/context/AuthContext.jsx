import React, { createContext, useState, useEffect} from 'react';
import {useNavigate} from "react-router-dom";
import {getCurrentUser} from "../APIs/user.js";
import * as authenticationService from "@/APIs/authentication.js";

export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUser = async () => {
            if(localStorage.getItem("accessToken") !== null) {
                const response = await getCurrentUser();
                setUser(response);
            }else{
                navigate("/login");
            }
        }
        fetchUser();
    }, []);

    const handleLogout = async () => {
        await authenticationService.logout();
        localStorage.clear();
        window.location.href = "/login";
    }

    return (
        <AuthContext.Provider value={{ user, setUser, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
