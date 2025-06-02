import {useContext, useState} from "react";
import "../styles/register.css";
import {signUp} from "../APIs/authentication.js";
import * as userService from "@/APIs/user.js";
import AuthContext from "@/context/AuthContext.jsx";

const Register = () => {
  const {setUser} = useContext(AuthContext);
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirm: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async () => {
    try {
      const response = await signUp(form.email, form.password, form.username);
      if(response.message === "User already exists!") alert("Email đã tồn tại");
      else if(response.message === "Signup successfully!") {
        alert("Tạo tài khoản thành công");
        const user = await userService.getCurrentUser();
        setUser(user);
        localStorage.setItem("user", JSON.stringify(user));
        localStorage.setItem("accessToken", response.accessToken);
        localStorage.setItem("refreshToken", response.accessToken);
        // window.location.href = "/";
      }
      else{
        alert("Có lỗi xảy ra");
      }
    }catch (error) {
      console.log(error);
      alert("Có lỗi xảy ra");
    }
  };

  return (
    <div className="login-page">
      <div className="login-left">
        <h1 className="fb-logo">PTIT CONNECT</h1>
        <p>Đăng ký tài khoản để kết nối với cộng đồng sinh viên PTIT.</p>
      </div>
      <div className="login-right">
        <div className="login-box">
          <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
            Tạo tài khoản
          </h2>
          <input
              type="text"
              name="username"
              placeholder="Tên người dùng"
              value={form.username}
              onChange={handleChange}
              required
          />
          <input
              type="email"
              name="email"
              placeholder="Email"
              value={form.email}
              onChange={handleChange}
              required
          />
          <input
              type="password"
              name="password"
              placeholder="Mật khẩu"
              value={form.password}
              onChange={handleChange}
              required
          />
          <input
              type="password"
              name="confirm"
              placeholder="Nhập lại mật khẩu"
              value={form.confirm}
              onChange={handleChange}
              required
          />
          <button type="submit" className="register-btn" onClick={handleRegister}>
            Đăng ký
          </button>
          <a href="/login" className="forgot">
            Đã có tài khoản? Đăng nhập
          </a>
        </div>
      </div>
    </div>
  );
};

export default Register;
