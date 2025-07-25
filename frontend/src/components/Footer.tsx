import React from 'react';
import { FaLinkedin } from 'react-icons/fa';

const Footer: React.FC = () => {
  return (
    <footer className="bg-gray-900 text-gray-300 py-6 mt-8">
      <div className="container mx-auto px-4 text-center flex flex-col items-center gap-2">
        <div className="flex items-center justify-center gap-2">
          <span>Made by Shriram Kulkarni – Founder of BeastXfitness AI</span>
          <a href="https://www.linkedin.com/in/shriram-kulkarni-033b8328a" target="_blank" rel="noopener noreferrer" className="text-blue-400 hover:text-blue-600 ml-2">
            <FaLinkedin size={20} />
          </a>
        </div>
        <p className="text-xs text-gray-400 mt-1 max-w-xl mx-auto">
          Disclaimer: This application provides general fitness guidance and is not a substitute for professional medical advice.
        </p>
      </div>
    </footer>
  );
};

export default Footer;