import { useState } from "react";
import "../styles/register.css";

const Register = () => {
  const [form, setForm] = useState({
    fullname: "",
    username: "",
    email: "",
    password: "",
    confirm: "",
  });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = (e) => {
    e.preventDefault();
    if (form.password !== form.confirm) {
      setError("Mật khẩu không khớp!");
      return;
    }
    alert(`Chào mừng ${form.fullname}! Bạn đã đăng ký thành công.`);
    setError("");
  };

  return (
    <div className="login-page">
      <div className="login-left">
        <h1 className="fb-logo">PTIT CONNECT</h1>
        <p>Đăng ký tài khoản để kết nối với cộng đồng sinh viên PTIT.</p>
      </div>
      <div className="login-right">
        <form className="login-box" onSubmit={handleRegister}>
          <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
            Tạo tài khoản
          </h2>
          <input
            type="text"
            name="fullname"
            placeholder="Họ và tên"
            value={form.fullname}
            onChange={handleChange}
            required
          />
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
          <button type="submit" className="register-btn">
            Đăng ký
          </button>
          {error && <p className="error">{error}</p>}
          <a href="/login" className="forgot">
            Đã có tài khoản? Đăng nhập
          </a>
        </form>
      </div>
    </div>
  );
};

export default Register;
