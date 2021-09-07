package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card(String name, CardType type) {
		cardName = name;
		cardType = type;
	}
	
	public boolean equals(Card card) {
		if (cardName.equals(card.cardName))
			return true;
		else
			return false;
	}

	public String getCardName() {
		return cardName;
	}

	public CardType getCardType() {
		return cardType;
	}
}
