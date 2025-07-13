import { Mic, LineChart, Lock } from "lucide-react";
import FeaturesListItem from "./FeaturesListItem";

const features = [
  {
    icon: Mic,
    title: "Voice input sale logs",
    subtitle: "Record sales data with simple voice commands",
  },
  {
    icon: LineChart,
    title: "Smart reports & insights",
    subtitle: "Get AI-powered analytics and recommendations",
  },
  {
    icon: Lock,
    title: "Encrypted, private & secure",
    subtitle: "Bank-level security for all your business data",
  },
];

const FeaturesCard = () => {
  return (
    <div className="bg-yellow-200 w-full h-full rounded-xl p-10 flex flex-col gap-8 max-w-sm">
      <h2 className="text-3xl font-extrabold leading-snug text-gray-900 max-w-[10ch]">Simplify your business</h2>
      <div className="flex flex-col gap-6">
        {features.map((feat) => (
          <FeaturesListItem key={feat.title} {...feat} />
        ))}
      </div>
    </div>
  );
};

export default FeaturesCard;
