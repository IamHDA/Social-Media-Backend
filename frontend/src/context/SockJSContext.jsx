import React, {createContext, useRef} from 'react';
import SockJS from "sockjs-client";
import {Client} from '@stomp/stompjs';

export const SockJSContext = createContext();

export const SockJSProvider = ({children}) => {
    const stompClientRef = useRef(null);
    const subscriptions = useRef({});

    const setUpStompClient = (groupIds, userId, onMessageReceived, onChatNoticeReceived, onNotificationReceived) => {
        return new Promise((resolve, reject) => {
            const socket = new SockJS("http://100.114.40.116:8081/ws");
            const stompClient = new Client({
                webSocketFactory: () => socket,
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: () => {
                    try {
                        if(localStorage.getItem('subscriptions')) {
                            subscriptions.current = JSON.parse(localStorage.getItem('subscriptions'));
                        }
                        if (userId) {
                            subscriptions.current[userId + "_message"] = stompClient.subscribe(
                                `/user/${userId}/queue/messages`,
                                onMessageReceived
                            );
                            subscriptions.current[userId + "_chatNotice"] = stompClient.subscribe(
                                `/user/${userId}/queue/chatNotices`,
                                onChatNoticeReceived
                            );
                        }
                        if (onNotificationReceived) {
                            subscriptions.current[userId + "_notification"] = stompClient.subscribe(
                                `/user/${userId}/queue/notification`,
                                onNotificationReceived
                            );
                        }
                        if (groupIds && onMessageReceived) {
                            groupIds.forEach(groupId => {
                                subscriptions.current[groupId] = stompClient.subscribe(
                                    `/topic/group/${groupId}`,
                                    onMessageReceived
                                );
                            });
                        }
                        stompClientRef.current = stompClient;
                        localStorage.setItem('subscriptions', JSON.stringify(subscriptions.current));
                        resolve(true);
                    } catch (err) {
                        reject(err);
                    }
                },
                onStompError: (frame) => {
                    const error = new Error(`STOMP error: ${frame.headers.message}\n${frame.body}`);
                    console.error(error);
                    reject(error);
                },
                onWebSocketError: (error) => {
                    console.error('WebSocket error:', error);
                    reject(error);
                },
                onDisconnect: () => {
                    console.log('STOMP Disconnected');
                }
            });
            stompClient.activate();
        });
    };

    const unsubscribe = (subscriptionId) => {
        if (stompClientRef.current) {
            stompClientRef.current.unsubscribe(subscriptions.current[subscriptionId]);
        }
    }

    const disconnectStomp = () => {
        if (stompClientRef.current) {
            stompClientRef.current.deactivate().then(() => {
                console.log('STOMP Client deactivated');
            });
        }
    };

    return (
        <SockJSContext.Provider value={{setUpStompClient, disconnectStomp, stompClientRef, unsubscribe}}>
            {children}
        </SockJSContext.Provider>
    );
};

export default SockJSContext;