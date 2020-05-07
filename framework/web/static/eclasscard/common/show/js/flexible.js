//适配屏幕密度
(function (doc, win) {	
	var docEl = doc.documentElement;
	var metaEl = doc.querySelector('meta[name="viewport"]');
	var scale = 0;
	var devicePixelRatio = win.devicePixelRatio;
	scale = 1/devicePixelRatio;
	metaEl.setAttribute('content', 'width=device-width,initial-scale=' + scale + ', maximum-scale=' + scale + ', minimum-scale=' + scale + ', user-scalable=no');
	if (docEl.firstElementChild) {
		docEl.firstElementChild.appendChild(metaEl);
	} else {
			var wrap = doc.createElement('div');
			wrap.appendChild(metaEl);
			doc.write(wrap.innerHTML);
	}
})(document, window);