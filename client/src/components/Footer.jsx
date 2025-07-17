import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-900 text-gray-300 py-8">
      <div className="max-w-6xl mx-auto px-6 flex flex-col sm:flex-row justify-between items-center gap-4">
        <p>&copy; {new Date().getFullYear()} Dukandar Digital Hisab. All rights reserved.</p>
        <div className="flex gap-4 text-sm">
          <a
            href="https://github.com/your-repo"
            className="hover:text-white transition"
            target="_blank"
            rel="noopener noreferrer"
          >
            GitHub
          </a>
          <a href="mailto:support@example.com" className="hover:text-white transition">
            Contact Support
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
