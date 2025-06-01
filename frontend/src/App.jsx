import React from "react";
import {SockJSProvider} from "@/context/SockJSContext.jsx";
import AppRoutes from "@/routes/AppRoutes.jsx";
import {AuthProvider} from "@/context/AuthContext.jsx";
import {SkeletonTheme} from "react-loading-skeleton";
import {CurtainProvider} from "@/context/CurtainContext.jsx";
import {FriendRequestProvider} from "@/context/FriendRequestContext.jsx";

function App() {


    return (
        <AuthProvider>
            <SockJSProvider>
                <CurtainProvider>
                    <FriendRequestProvider>
                        <AppRoutes />
                    </FriendRequestProvider>
                </CurtainProvider>
            </SockJSProvider>
        </AuthProvider>
    );
}

export default App;
