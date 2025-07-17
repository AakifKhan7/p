import React from 'react';

const features = [
  {
    title: 'Phone-based Login',
    description: 'OTP-based simple login system for quick access.',
    emoji: '🔐',
  },
  {
    title: 'Voice Sale Entry',
    description: 'Speak item & amount to automatically log sales.',
    emoji: '🎙️',
  },
  {
    title: 'Daily Dashboard',
    description: "View today's sales summary with insightful charts.",
    emoji: '📊',
  },
  {
    title: 'PDF Report Generator',
    description: 'Download or share weekly/monthly PDF reports.',
    emoji: '🧾',
  },
  {
    title: 'Sales History',
    description: 'Browse full list of past entries with filters.',
    emoji: '📚',
  },
  {
    title: 'Trial & Activation',
    description: '7-day free usage, then easy UPI payment activation.',
    emoji: '🕒',
  },
];

const FeaturesSection = () => {
  return (
    <section id="features" className="py-20 bg-gray-50">
      <div className="max-w-6xl mx-auto px-6">
        <h2 className="text-3xl md:text-4xl font-bold text-center mb-12">Power-packed Features</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((f) => (
            <div
              key={f.title}
              className="bg-white p-6 rounded-xl shadow hover:shadow-lg transition flex flex-col items-start"
            >
              <div className="text-4xl mb-4">{f.emoji}</div>
              <h3 className="text-xl font-semibold mb-2">{f.title}</h3>
              <p className="text-gray-600 leading-relaxed">{f.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default FeaturesSection;
