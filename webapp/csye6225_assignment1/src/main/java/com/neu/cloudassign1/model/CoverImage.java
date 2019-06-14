package com.neu.cloudassign1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="coverimage")
public class CoverImage {
	
	@Id
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "imageId")
	private UUID imageId = UUID.randomUUID()	;
	
	@Column(name = "uri")
	private String uri;
	
	@Column(name = "bookId")
	private String bookId;
	
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "coverImage")
//	private Book book;
	
	
	public CoverImage() {
	}



	public CoverImage(String uri, String bookId) {
		this.uri = uri;
		this.bookId = bookId;
	}



	public UUID getImageId() {
		return imageId;
	}

	public void setImageId(UUID imageId) {
		this.imageId = imageId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

	
	
//	public Book getBook() {
//		return book;
//	}
//
//	public void setBook(Book book) {
//		this.book = book;
//	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	@Override
	public String toString() {
		return "CoverImage [imageId=" + imageId + ", uri=" + uri + "]";
	}
	
	

}
