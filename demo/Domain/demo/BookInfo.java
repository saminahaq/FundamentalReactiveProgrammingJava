package demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data //forlombok
@AllArgsConstructor
@NoArgsConstructor 
public class BookInfo {
	private long BookId;
	private String title;
	private String author;
	private String ISBN;
}

