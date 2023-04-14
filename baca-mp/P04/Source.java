import java.util.Scanner;
class Car {					
	private String _name;	
	private Car _next;		
	private Car _prev;		
	public Car(final String name, final Car prev, final Car next) { 	
		this._name = name;
		this._next = next;
		this._prev = prev;
	}
	public Car(final String name) {		
		this._name = name;
		this._next = this;
		this._prev = this;
	}
	public String getName() {
		return _name;
	}
	public Car getPrev() {
		return _prev;
	}
	public Car getNext() {
		return _next;
	}
	public void hookNext(final Car next) {
		_next = next;
	}
	public void hookPrev(final Car prev) {
		_prev = prev;
	}
	public void reverseHooks() { 
		Car tmp = _prev;
		_prev = _next;
		_next = tmp;
	}
}
class Train {
	private String _name;	
	private Train _next;	
	private Car _first;		
	public Train(final String trainName, final String carName, final Train next) {	
		this._name = trainName;			
		this._first = new Car(carName);	
		this._next = next;
	}
	public boolean isEqual(final String trainName) {	
		return this._name.equals(trainName);
	}
	public boolean isSingleCar() {						
		return this._first.getNext() == this._first && this.getLast().getPrev() == this._first;
	}
	public String getName() {
		return _name;
	}
	public Train getNext() {
		return _next;
	}
	public Car getFirst() {
		return this._first;
	}
	public Car getLast() {
		return this._first.getPrev();
	}
	public String getCarListStr() {
		StringBuilder	carListString	= new StringBuilder(""); 	
		Car				curCar			= this._first;	
		Car				prevCar			= null;			
		do {
			carListString.append(" ").append(curCar.getName());		
			if (curCar.getNext() != prevCar) {
				prevCar = curCar;
				curCar = curCar.getNext();
			} else {
				prevCar = curCar;
				curCar = curCar.getPrev();
			}
		} while (curCar != this._first);	
		return carListString.toString();	
	}
	public void hookNext(final Train next) {
		_next = next;
	}
	public void insertLastCar(final String carName) {		
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
	}
	public void insertFirstCar(final String carName) {  
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
		this._first = insertedCar;
	}
	public void reverse() {
		Car last = this.getLast();
		last.reverseHooks();		
		this._first.reverseHooks();	
		this._first = last;			
	}
	public String deleteLastCar() {	
		Car	oldLast	= this.getLast();	
		Car	newLast	= oldLast.getPrev();
		if (oldLast.getPrev().getNext() != oldLast) {	
			newLast.hookPrev(newLast.getNext());		
		}
		newLast.hookNext(_first);		
		this._first.hookPrev(newLast);
		return oldLast.getName();	
	}
	public String deleteFirstCar() {
		Car	oldFirst	= this.getFirst();	
		Car	last		= this.getLast();	
		Car	newFirst	= _first.getNext();	
		if (oldFirst.getNext().getPrev() != oldFirst) {	
			newFirst.hookNext(newFirst.getPrev());		
		}
		last.hookNext(newFirst); 	
		newFirst.hookPrev(last);
		_first = newFirst;			
		return oldFirst.getName();
	}
}
class TrainList {			
	private Train _trains;	
	public TrainList() {	
		this._trains = null;
	}
	public TrainList(final Train train) {	
		this._trains = train;
	}
	public Train findTrain(String trainName) {
		for (Train curTrain = this._trains; curTrain != null; curTrain = curTrain.getNext()) { 
			if (curTrain.isEqual(trainName)) { 	
				return curTrain;				
			}
		}
		return null; 
	}
	public Train[] findTwoTrainsAndTrainBefore(final String tNameA, final String tNameB) {
		Train	trainA			= null;
		Train	trainBeforeB	= null;
		Train	trainB			= null;
		if (this._trains != null && this._trains.isEqual(tNameB)) {	
			trainBeforeB = null;			
			trainB = this._trains;			
		}
		for (Train curTrain = this._trains; 
				curTrain != null; 
				curTrain = curTrain.getNext()) {
			if (curTrain.isEqual(tNameA)) {	
				trainA = curTrain;
				if (trainB != null) {
					break;
				}	
			}
			if (curTrain.getNext() != null && curTrain.getNext().isEqual(tNameB)) {	
				trainBeforeB = curTrain;
				trainB = curTrain.getNext();
				if (trainA != null) {
					break;
				}	
			}
		}
		return new Train[] { trainA, trainBeforeB, trainB };	
	}
	public String New(final String trainName, final String carName) {	
		if (this.findTrain(trainName) != null) {	
			return Error.getErrorStr(Error.trainExists, trainName);
		}
		this._trains = new Train(trainName, carName, this._trains);	
		return "";	
	}
	public String InsertLast(final String trainName, final String carName) {	
		Train curTrain = findTrain(trainName);
		if (curTrain == null) {		
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		curTrain.insertLastCar(carName);	
		return "";		
	}
	public String InsertFirst(final String trainName, final String carName) {	
		Train curTrain = findTrain(trainName);
		if (curTrain == null) {		
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		curTrain.insertFirstCar(carName);  
		return "";		
	}
	public String GetTrainStr(final String trainName) {		
		Train curTrain = findTrain(trainName);
		if (curTrain == null) {		
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		StringBuilder carList = new StringBuilder(curTrain.getName());		
		carList.append(":").append(curTrain.getCarListStr()).append("\n");	
		return carList.toString();											
	}
	public String Reverse(final String trainName) {		
		Train curTrain = findTrain(trainName);
		if (curTrain == null) {		
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		curTrain.reverse();	
		return "";	
	}
	public String Union(final String tNameA, final String tNameB) {	
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameA, tNameB);
		Train	trainA			= TrainArr[0];
		Train	trainBeforeB	= TrainArr[1];	
		Train	trainB			= TrainArr[2];
		if (trainA == null) {	
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainB == null) {	
			return Error.getErrorStr(Error.trainNotExists, tNameB);
		}
		Car	trainA_last	= trainA.getLast();				
		Car	trainB_last	= trainB.getLast();				
		trainA_last.hookNext(trainB.getFirst());		
		trainB.getFirst().hookPrev(trainA_last);		
		trainA.getFirst().hookPrev(trainB_last);		
		trainB_last.hookNext(trainA.getFirst());		
		this.DeleteNext(trainBeforeB);					
		return "";
	}
	public void DeleteNext(final Train trainBefore) {			
		if (trainBefore == null) {			
			_trains = _trains.getNext();
			return;
		}
		trainBefore.hookNext(trainBefore.getNext().getNext());	
	}
	public String Trains() {					
		StringBuilder trainsSB = new StringBuilder("Trains:");		
		for (Train curTrain = _trains; curTrain != null; curTrain = curTrain.getNext()) { 
			trainsSB.append(" ").append(curTrain.getName());
		}
		return trainsSB.append("\n").toString();	
	}
	public String delLast(final String tNameA, final String tNameB) {	
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);
		Train	trainBeforeA	= TrainArr[1];  
		Train	trainA			= TrainArr[2];
		Train	trainB			= TrainArr[0];
		if (trainB != null) {	
			return Error.getErrorStr(Error.trainExists, tNameB);
		}
		if (trainA == null) {	
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainA.isSingleCar()) {	
			String deletedName = trainA.getFirst().getName();
			this.DeleteNext(trainBeforeA);
			this._trains = new Train(tNameB, deletedName, this._trains);	
			return "";	
		}
		String deletedName = trainA.deleteLastCar();	
		this._trains = new Train(tNameB, deletedName, this._trains);				
		return "";  
	}
	public String delFirst(final String tNameA, final String tNameB) {
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);
		Train	trainBeforeA	= TrainArr[1];		
		Train	trainA			= TrainArr[2];
		Train	trainB			= TrainArr[0];
		if (trainB != null) {	
			return Error.getErrorStr(Error.trainExists, tNameB);
		}
		if (trainA == null) {	
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainA.isSingleCar()) {  
			String deletedName = trainA.getFirst().getName();
			this.DeleteNext(trainBeforeA);	
			this._trains = new Train(tNameB, deletedName, this._trains);  
			return "";	
		}
		String deletedName = trainA.deleteFirstCar();  
		this._trains = new Train(tNameB, deletedName, this._trains); 		
		return "";						 	
	}
}
enum Error { 
	trainExists,	
	trainNotExists;	
	public static String getErrorStr(final Error errName, final String trainName) { 
		switch (errName) {
			case trainExists:
				return "Train ".concat(trainName).concat(" already exists\n");
			case trainNotExists:
				return "Train ".concat(trainName).concat(" does not exist\n");
			default:
		}
		return "";
	}
}
public class Source {
	public static Scanner sc = new Scanner(System.in);	
	public static void main(String[] args) {
		long			test_count	= sc.nextLong();	
		StringBuilder	output		= new StringBuilder();	
		while (test_count-- > 0) {	
			long		OP_count	= sc.nextLong();		
			TrainList	trainList	= new TrainList();		
			while (OP_count-- > 0) {
				String	OP_resultStr	= "";			
				String	OP				= sc.next(); 
				switch (OP) {
					case "New": {
						String	trainName	= sc.next();
						String	carName		= sc.next();
						OP_resultStr = trainList.New(trainName, carName);
						break;
					}
					case "InsertFirst": {
						String	trainName	= sc.next();
						String	carName		= sc.next();
						OP_resultStr = trainList.InsertFirst(trainName, carName);
						break;
					}
					case "InsertLast": {
						String	trainName	= sc.next();
						String	carName		= sc.next();
						OP_resultStr = trainList.InsertLast(trainName, carName);
						break;
					}
					case "Display": {
						String trainName = sc.next();
						OP_resultStr = trainList.GetTrainStr(trainName);
						break;
					}
					case "Trains": {
						OP_resultStr = trainList.Trains();
						break;
					}
					case "Reverse": {
						String trainName = sc.next();
						OP_resultStr = trainList.Reverse(trainName);
						break;
					}
					case "Union": {
						String	trainNameA	= sc.next();
						String	trainNameB	= sc.next();
						OP_resultStr = trainList.Union(trainNameA, trainNameB);
						break;
					}
					case "DelFirst": {
						String	trainNameA	= sc.next();
						String	trainNameB	= sc.next();
						OP_resultStr = trainList.delFirst(trainNameA, trainNameB);
						break;
					}
					case "DelLast":
						String trainNameA = sc.next();
						String trainNameB = sc.next();
						OP_resultStr = trainList.delLast(trainNameA, trainNameB);
						break;
				}
				output.append(OP_resultStr);	
			}
		}
		System.out.print(output.toString()); 
	}
}
