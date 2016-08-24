package com.example.asus.dantrirss.utils;

public class RSSItem {

	String _title;
	String _link;
	String _description;
	String _pubdate;
	String _img;

	// constructor
	public RSSItem() {

	}

	public RSSItem(String title, String link, String description,
			String pubdate, String img) {
		this._title = title;
		this._link = link;
		this._description = description;
		this._pubdate = pubdate;
		this._img = img;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_link() {
		return _link;
	}

	public void set_link(String _link) {
		this._link = _link;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public String get_pubdate() {
		return _pubdate;
	}

	public void set_pubdate(String _pubdate) {
		this._pubdate = _pubdate;
	}

	public String get_img() {
		return _img;
	}

	public void set_img(String _img) {
		this._img = _img;
	}

}