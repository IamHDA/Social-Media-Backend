import {useContext, useState} from "react";
import { login } from "../APIs/authentication";
import * as userService from "../APIs/user.js";
import {useNavigate} from "react-router-dom";
import "../styles/login.css";
import AuthContext from "@/context/AuthContext.jsx";

const Login = () => {
  const {setUser} = useContext(AuthContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await login(email, password);
      if(response.message === "User not found!") alert("Tài khoản không tồn tại");
      else if(response.message === "Wrong password!") alert("Sai mật khẩu");
      else if(response.message === "User login successfully!"){
        localStorage.setItem("accessToken", response.accessToken);
        localStorage.setItem("refreshToken", response.refreshToken);
        const user = await userService.getCurrentUser();
        setUser(user);
        localStorage.setItem("user", JSON.stringify(user));
        navigate("/");
      }else alert("Có lỗi xảy ra");
    } catch (error) {
      console.log(error);
      alert("Có lỗi xảy ra")
    }
  };

  const handleForgotPassword = (e) => {
    e.preventDefault();
    navigate("/forgot-password");
  };

  const handleRegister = () => {
    navigate("/register");
  };

  return (
    <div className="login-page">
      <div className="login-left">
        <h1 className="fb-logo">PTIT CONNECT</h1>
        <p>
          PTIT CONNECT giúp bạn kết nối và chia sẻ với mọi người trong cuộc sống
          của bạn.
        </p>
      </div>

      <div className="login-right">
        <form className="login-box" onSubmit={handleSubmit}>
          <input
            type="text"
            name="username"
            placeholder="Email hoặc số điện thoại"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            name="password"
            placeholder="Mật khẩu"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit" className="login-btn">
            Đăng nhập
          </button>
          <a href="#" className="forgot" onClick={handleForgotPassword}>
            Quên mật khẩu?
          </a>
          <hr />
          <button
            type="button"
            className="register-btn"
            onClick={handleRegister}
          >
            Tạo tài khoản mới
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
