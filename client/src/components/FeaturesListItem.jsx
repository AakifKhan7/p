import { Check } from "lucide-react";

const FeaturesListItem = ({ icon: Icon = Check, title, subtitle }) => {
  return (
    <div className="flex items-start gap-3">
      <div className="bg-yellow-300 p-2 rounded-full inline-flex items-center justify-center mt-1">
        <Icon size={18} className="text-black" />
      </div>
      <div>
        <h4 className="font-semibold text-sm text-gray-900">{title}</h4>
        <p className="text-xs text-gray-600 leading-snug">{subtitle}</p>
      </div>
    </div>
  );
};

export default FeaturesListItem;
