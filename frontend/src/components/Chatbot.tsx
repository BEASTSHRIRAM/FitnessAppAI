import { useEffect } from 'react';

const BotpressChat: React.FC = () => {
  useEffect(() => {
    // Inject Botpress webchat scripts
    const script1 = document.createElement('script');
    script1.src = 'https://cdn.botpress.cloud/webchat/v3.2/inject.js';
    script1.defer = true;
    document.body.appendChild(script1);

    const script2 = document.createElement('script');
    script2.src = 'https://files.bpcontent.cloud/2025/07/16/18/20250716182059-77Z11CPV.js';
    script2.defer = true;
    document.body.appendChild(script2);

    return () => {
      document.body.removeChild(script1);
      document.body.removeChild(script2);
    };
  }, []);

  return null; // The Botpress widget will appear automatically
};

export default BotpressChat; 