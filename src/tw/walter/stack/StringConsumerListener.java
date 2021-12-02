package tw.walter.stack;

/*
 * Interface that the class who wants listen StringConsumer event must implements
 */
public interface StringConsumerListener {

	// Method called on StringConsumer get
	void onStringConsumerGet(char c);

	// Method called on StringConsumer reverse
	void onStringConsumerReverse(char c);
	
}
