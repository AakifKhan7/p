import { Mail, Lock } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import InputField from "./InputField";

const LoginForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });
      if (!res.ok) {
        throw new Error("Invalid credentials");
      }
      const data = await res.json();
      localStorage.setItem("token", data.accessToken);
      navigate("/");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="w-full max-w-md flex flex-col gap-6">
      <InputField
        name="email"
        label="Email address"
        type="email"
        icon={Mail}
        placeholder="Enter your email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <InputField
        name="password"
        label="Password"
        type="password"
        icon={Lock}
        placeholder="Enter your password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <div className="flex items-center justify-between text-xs text-gray-600">
        <div className="flex items-center gap-2">
          <input type="checkbox" id="remember" className="accent-yellow-500" />
          <label htmlFor="remember" className="select-none">Remember me</label>
        </div>
        <Link to="#" className="text-yellow-600 font-medium hover:underline">Forgot password?</Link>
      </div>

      <button
        type="submit"
        className="mt-1 bg-yellow-500 hover:bg-yellow-600 text-black font-semibold py-2 rounded-md transition-colors"
      >
        Sign in
      </button>

      <p className="text-center text-xs text-gray-600">
        Don't have an account? {" "}
        <Link to="/signup" className="text-yellow-600 font-medium hover:underline">Create account</Link>
      </p>
    </form>
  );
};

export default LoginForm;
