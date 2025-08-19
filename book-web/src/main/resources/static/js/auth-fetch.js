(function(){
  if (window.__authFetchInstalled) return;
  window.__authFetchInstalled = true;
  const originalFetch = window.fetch;

  let refreshPromise = null;

  async function ensureAccessToken() {
    if (refreshPromise) return refreshPromise;
    refreshPromise = (async () => {
      const resp = await originalFetch('/auth/refresh', {
        method: 'POST',
        credentials: 'same-origin',
        headers: { 'Accept': 'application/json' }
      });
      if (!resp.ok) throw new Error('토큰 갱신 실패');
      const data = await resp.json();
      if (data && data.accessToken) {
        try { localStorage.setItem('accessToken', data.accessToken); } catch(_) {}
      }
      return data;
    })();
    try {
      return await refreshPromise;
    } finally {
      refreshPromise = null;
    }
  }

  function withAuth(init){
    const token = (typeof localStorage !== 'undefined') ? localStorage.getItem('accessToken') : null;
    if (!token) return init;
    let headers = new Headers();
    if (init && init.headers) new Headers(init.headers).forEach((v,k)=>headers.set(k,v));
    if (!headers.has('Authorization')) headers.set('Authorization', 'Bearer ' + token);
    return Object.assign({}, init, { headers });
  }

  window.fetch = async function(input, init){
    const reqUrl = typeof input === 'string' ? input : (input && input.url) || '';
    const isAbsolute = /^https?:\/\//i.test(reqUrl);
    const isSameOrigin = !isAbsolute || reqUrl.startsWith(window.location.origin);
    const firstInit = isSameOrigin ? withAuth(init) : init;

    let resp = await originalFetch.call(this, input, firstInit);

    if (isSameOrigin && (resp.status === 401 || resp.status === 403)) {
      try {
        await ensureAccessToken();
        const retryInit = withAuth(init);
        resp = await originalFetch.call(this, input, retryInit);
      } catch (e) {
        try { localStorage.removeItem('accessToken'); } catch(_) {}
        return resp;
      }
    }

    return resp;
  };
})();