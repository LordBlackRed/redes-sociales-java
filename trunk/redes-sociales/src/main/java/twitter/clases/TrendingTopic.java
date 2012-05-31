package twitter.clases;

public class TrendingTopic implements Comparable<TrendingTopic> {

	private Integer numHashTagRepetidos;
	private String hashtag;

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public Integer getNumHashTagRepetidos() {
		return numHashTagRepetidos;
	}

	public void setNumHashTagRepetidos(Integer numHashTagRepetidos) {
		this.numHashTagRepetidos = numHashTagRepetidos;
	}

	public TrendingTopic() {
		super();
	}

	public TrendingTopic(String hashtag, Integer numHashTagRepetidos) {
		super();
		this.hashtag = hashtag;
		this.numHashTagRepetidos = numHashTagRepetidos;
	}

	public int compareTo(TrendingTopic arg0) {
		return arg0.getNumHashTagRepetidos() - this.getNumHashTagRepetidos();
	}

}
