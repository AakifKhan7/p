import { Building2 } from "lucide-react";

const Logo = () => {
  return (
    <div className="flex items-center gap-2 select-none">
      {/* Icon inside yellow square */}
      <span className="bg-yellow-400/90 rounded-lg p-2 inline-flex items-center justify-center">
        <Building2 size={20} className="text-black" />
      </span>
      <span className="font-bold text-xl text-black tracking-wide">Rentally</span>
    </div>
  );
};

export default Logo;
