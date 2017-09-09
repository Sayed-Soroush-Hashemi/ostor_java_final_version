package hardware.cpu;

public class Value {
	protected String value;
	protected ValueType type;

	public Value(String value){
		this.value = value;
		this.type = ValueType.STRING;
	}

	public Value(Integer value){
		this.value = String.valueOf(value);
		this.type = ValueType.INTEGER;
	}

	public Value(boolean value){
		this.value = (value ? "1" : "0");
		this.type = ValueType.INTEGER;
	}

	public Value(String value, ValueType type) {
		this.value = value;
		this.type = type;
	}

	public Value(Number value, ValueType type) {
		this.value = String.valueOf(value);
		this.type = type;
	}

	public Value add(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() + val2.toInteger());
		if(this.isString() && val2.isString())
			return new Value(this.toString() + val2.toString());
		
		// TODO: throw exception
		return null;
	}

	public Value subtract(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() - val2.toInteger());
		// TODO: throw exception
		return null;
	}
	public Value multiply(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() * val2.toInteger());
		// TODO: throw exception
		return null;
	}
	public Value divide(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() / val2.toInteger());
		// TODO: throw exception
		return null;
	}
	public Value mod(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() % val2.toInteger());
		// TODO: throw exception
		return null;
	}

	//manteghi
	public Value and(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() & val2.toInteger());
		// TODO: throw exception
		return null;
	}

	public Value or(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() | val2.toInteger());
		// TODO: throw exception
		return null;
	}

	public Value xor(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return new Value(this.toInteger() ^ val2.toInteger());
		// TODO: throw exception
		return null;
	}

	public Value not() {
		if(this.isInteger())
			return new Value(1 - this.toInteger());
		// TODO: throw exception
		return null;
	}

	public boolean isEqual(Value val2) {
		return (this.getValue().equals(val2.getValue()) 
				&& this.getType() == val2.getType());
	}

	public boolean isNotEqual(Value val2) {
		return !isEqual(val2);
	}

	public boolean isGreaterThan(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return this.toInteger() > val2.toInteger();
		// TODO: throw exception
		return false;
	}
	
	public boolean isGreaterThanOrEqual(Value val2) {
		return this.isEqual(val2) | this.isGreaterThan(val2);
	}
	
	public boolean isLessThan(Value val2) {
		if(this.isInteger() && val2.isInteger())
			return this.toInteger() < val2.toInteger();
		// TODO: throw exception
		return false;
	}
	
	public boolean isLessThanOrEqual(Value val2) {
		return this.isEqual(val2) | this.isLessThan(val2);
	}

	public boolean isInteger(){
		return type == ValueType.INTEGER;
	}
	public boolean isString(){
		return type == ValueType.STRING;
	}

	public int toInteger(){
		return Integer.valueOf(this.value);
	}
	public String toString(){
		return this.value;
	}
	
	public String getValue(){
		return this.value;
	}
	public ValueType getType(){
		return this.type;
	}

}

