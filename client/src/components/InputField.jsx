import { Eye, EyeOff } from "lucide-react";
import { useState } from "react";

const InputField = ({ label, type = "text", icon: Icon, placeholder, value, onChange, name }) => {
  const [show, setShow] = useState(false);
  const isPassword = type === "password";
  const currentType = isPassword && show ? "text" : type;

  return (
    <div className="flex flex-col gap-1 w-full">
      <label className="font-medium text-sm text-gray-700">{label}</label>
      <div className="relative w-full">
        {Icon && (
          <Icon className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
        )}
        <input
          name={name}
          type={currentType}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          className="w-full border border-gray-300 rounded-md py-2 pl-10 pr-10 text-sm focus:outline-none focus:border-yellow-500"
        />
        {isPassword && (
          <button
            type="button"
            onClick={() => setShow((s) => !s)}
            className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 focus:outline-none"
          >
            {show ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
        )}
      </div>
    </div>
  );
};

export default InputField;
