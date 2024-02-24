const win = Ti.UI.createWindow({
	layout: "vertical"
});
const mediaControl = require('ti.mediacontrol');
const btn = Ti.UI.createButton({title: "create player"});
const btn_change = Ti.UI.createButton({title: "change"});
const btn_close = Ti.UI.createButton({title: "close"});
const btn_play = Ti.UI.createButton({title: "play"});
const btn_pause = Ti.UI.createButton({title: "pause"});

btn.addEventListener("click", function() {
	mediaControl.createPlayer({
		title: "-",
		text: "-",
		color: "#00f",
		showPrevious: true,
		showNext: true,
		backgroundImage: "/cover.jpg"
	});
})

btn_pause.addEventListener("click", function() {
	mediaControl.pause();
})
btn_play.addEventListener("click", function() {
	mediaControl.play();
})

btn_change.addEventListener("click", function() {
	mediaControl.backgroundImage = "/cover2.jpg"
	mediaControl.updateInfo({
		title: "music playing",
		text: "some radio station"
	})
	mediaControl.setMetadata({
		artist: "music playing",
		album: "music playing",
		title: "some radio station"
	})
})

btn_close.addEventListener("click", function() {
	mediaControl.close();
})

win.add([btn, btn_change, btn_close, btn_play, btn_pause]);
win.open();

const audioPlayer = Ti.Media.createAudioPlayer({
	url: 'http://streams.90s90s.de/hiphop/mp3-192/streams.90s90s.de/',
	allowBackground: true
});

var isPlaying = false;

mediaControl.addEventListener("keyPress", function(e) {
	console.log(e.keyCode)
});
mediaControl.addEventListener("changeStatus", function(e) {
	console.log("status: " + e.status);
	if (e.status == mediaControl.PAUSE) {
		isPlaying = false;
		console.log("pause");
		audioPlayer.pause();
		mediaControl.title = ""
		mediaControl.text = ""
	} else if (e.status == mediaControl.PLAY) {
		if (isPlaying) {
			isPlaying = false;
			console.log("pause");
			audioPlayer.pause();
			mediaControl.title = ""
			mediaControl.text = ""
		} else {
			isPlaying = true;
			console.log("play");
			audioPlayer.start();
			mediaControl.backgroundImage = "/cover2.jpg"
			mediaControl.updateInfo({
				title: "music playing",
				text: "some radio station"
			})

			mediaControl.setMetadata({
				artist: "music playing 2",
				album: "music playing 2",
				title: "some radio station 2"
			})
		}
	} else if (e.status == mediaControl.NEXT) {
		console.log("next");
	} else if (e.status == mediaControl.PREVIOUS) {
		console.log("previous");
	}
})
