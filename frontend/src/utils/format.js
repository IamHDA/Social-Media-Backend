export const getImageMime = (base64) => {
    if (base64.startsWith('/9j/')) return 'image/jpeg';
    if (base64.startsWith('iVBOR')) return 'image/png';
    if (base64.startsWith('R0lGOD')) return 'image/gif';
    if (base64.startsWith('UklGR')) return 'image/webp';
    return 'image/jpeg';
}

export const getFileFromUrl = async (url) => {
    const response = await fetch(url);
    const blob = await response.blob();
    return new File([blob], "sample.jpg", { type: blob.type });
}

export const formatTimeAgo = (sentTime) => {
    const now = new Date();
    const past = new Date(sentTime);
    const diffInMs = now - past;
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
    const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60));
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));
    const diffInMonths = Math.floor(diffInDays / 30);
    const diffInYears = Math.floor(diffInDays / 365);

    if (diffInYears > 0) return `${diffInYears} năm trước`;
    if (diffInMonths > 0) return `${diffInMonths} tháng trước`;
    if (diffInDays > 0) return `${diffInDays} ngày trước`;
    if (diffInHours > 0) return `${diffInHours} giờ trước`;
    if (diffInMinutes > 0) return `${diffInMinutes} phút trước`;

    return "Vừa xong";
}
