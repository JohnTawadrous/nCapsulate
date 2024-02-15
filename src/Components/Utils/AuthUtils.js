export const getJwtToken = () => {
    const cookiePairs = document.cookie.split(';');
    const jwtCookiePair = cookiePairs.find(pair => pair.trim().startsWith('myJwtCookie='));
  
    if (jwtCookiePair) {
      const jwtCookie = jwtCookiePair.trim().substring('myJwtCookie='.length);
      return jwtCookie;
    } else {
      console.error('JWT token not found or expired.');
      return '';
    }
  };