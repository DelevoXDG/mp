// Maksim Zdobnikau - 2

import java.util.Scanner;

// Section Todo
// [ ] rename to CarList
// [ ] PP kind reducing of checks by storing previously searched train
// [ ] Use stringbuilder and return error or normal strings to print the result faster
// [ ] Optimize find 2 algo

class Car {
	private String _name;
	private Car _next;
	private Car _prev;

	public Car(String name, Car prev, Car next) {
		this._name = name;
		this._next = next;
		this._prev = prev;
	}
	public Car(String name) {
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
	public void hookNext(Car next) {
		_next = next;
	}
	public void hookPrev(Car prev) {
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

	public Train(String trainName, String carName, Train next) {
		this._name = trainName;
		this._first = new Car(carName);
		this._next = next;
	}
	public Train(String trainName) {
		this._name = trainName;
	}
	public boolean isEqual(String trainName) {
		if (this._name.equals(trainName)) {
			return true;
		}
		return false;
	}
	public String getName() {
		return _name;
	}
	public Train getNext() {
		return _next;
	}
	public void hookNext(Train next) {
		_next = next;
	}
	public Car getFirst() {
		return this._first;
	}
	public Car getLast() {
		// Note: depends on first!!!
		return this._first.getPrev();
	}
	public void insertLastCar(String carName) {
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
	}
	public void insertFirstCar(String carName) {
		Car insertedCar = new Car(carName, this.getLast(), this._first);
		this.getLast().hookNext(insertedCar);
		this._first.hookPrev(insertedCar);
		this._first = insertedCar;
	}
	public String getCars() {
		StringBuilder	carListString	= new StringBuilder("");
		Car				curCar			= this._first;
		Car				prevCar			= null;
		do {
			carListString.append(" " + curCar.getName());
			if (curCar.getNext() == prevCar) {
				prevCar = curCar;
				curCar = curCar.getPrev();
			} else {
				prevCar = curCar;
				curCar = curCar.getNext();
			}
		} while (curCar != this._first);

		return carListString.toString();
	}
	public void reverse() {
		// Main idea: 
		// normal train 
		// this.next.prev == this
		// reversed train
		// this.next.prev != this

		Car last = this.getLast();
		last.reverseHooks();
		this.getFirst().reverseHooks();

		_first = last;
	}
	public String DeleteLast() {	//return's deleted train name

		// if (this.getLast())
		Car	oldLast	= this.getLast();
		Car	newLast	= oldLast.getPrev();

		if (oldLast.getPrev().getNext() != oldLast) {
			newLast.hookPrev(newLast.getNext());
		}

		newLast.hookNext(_first);
		this._first.hookPrev(newLast);
		return oldLast.getName();
	}
	public String DeleteFirst() {
		Car	oldFirst	= this.getFirst();
		Car	last		= this.getLast();
		Car	newFirst	= _first.getNext();

		if (oldFirst.getNext().getPrev() != oldFirst) {
			newFirst.hookNext(newFirst.getPrev());
		}
		// [x] case 1 car
		last.hookNext(newFirst);
		newFirst.hookPrev(last);
		_first = newFirst;
		return oldFirst.getName();
	}
	public boolean isSingleCar() {
		return this._first.getNext() == this._first && this.getLast().getPrev() == this._first;
	}
}

// Section List
class TrainList {
	private Train _trains;

	public TrainList() {
		this._trains = null;
	}
	public TrainList(Train train) {
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
	public Train[] findTwoTrainsAndTrainBefore(String tNameA, String tNameB) {
		Train	trainA			= null;
		Train	trainBeforeB	= null;
		Train	trainB			= null;

		if (this._trains.isEqual(tNameB)) {
			trainBeforeB = null;	// trainB jest pierwszy
			trainB = this._trains;
		}
		for (Train curTrain = this._trains; curTrain != null; curTrain = curTrain.getNext()) {
			if (curTrain.isEqual(tNameA)) {
				trainA = curTrain;
			}
			if (curTrain.getNext() != null && curTrain.getNext().isEqual(tNameB)) {
				trainBeforeB = curTrain;
				trainB = curTrain.getNext();
			}
		}
		return new Train[] { trainA, trainBeforeB, trainB };
	}

	public String New(String trainName, String carName) {
		// Wstawenie pociagu na poczatek listy, zeby spelnic zlozonosc O(1)
		Train curTrain = this.findTrain(trainName);

		if (curTrain == null) {
			curTrain = new Train(trainName, carName, this._trains);
			this._trains = curTrain;
		} else {
			return Error.getErrorStr(Error.trainExists, trainName);
		}
		return "";
	}
	public String insertLast(String trainName, String carName) {
		Train curTrain = findTrain(trainName);

		if (curTrain != null) {
			curTrain.insertLastCar(carName);
		} else {
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		return "";
	}
	public String insertFirst(String trainName, String carName) {
		Train curTrain = findTrain(trainName);

		if (curTrain != null) {
			curTrain.insertFirstCar(carName);
		} else {
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		return "";
	}

	public String printCarList(String trainName) {
		StringBuilder	carList		= new StringBuilder("");
		Train			curTrain	= findTrain(trainName);
		if (curTrain != null) {
			carList.append(curTrain.getName()).append(":").append(curTrain.getCars());
			System.out.println(carList);
		} else {
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		return "";
	}
	public String Reverse(String trainName) {
		Train curTrain = findTrain(trainName);
		if (curTrain != null) {
			curTrain.reverse();
		} else {
			return Error.getErrorStr(Error.trainNotExists, trainName);
		}
		return "";
	}
	public String Union(String tNameA, String tNameB) {
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
		trainA_last.hookNext(trainB.getFirst());		// Making end of trainA next-point ot start of trainB
		trainB.getFirst().hookPrev(trainA_last);		// Making start of trainB prev-point to end of trainA
		trainA.getFirst().hookPrev(trainB_last);		// Restoring circular structure prev-wise
		trainB_last.hookNext(trainA.getFirst());		// Restoring circular structure next-wise

		DeleteNext(trainBeforeB);
		return "";
	}

	public void DeleteNext(Train trainBefore) {
		if (trainBefore == null) {			// Przypadek, gdy usuwany element jest pierwszy ( nie ma "preva") 
			_trains = _trains.getNext();
			return;
		}
		trainBefore.hookNext(trainBefore.getNext().getNext());
	}
	public String getTrainsString() {
		StringBuilder trainsSB = new StringBuilder("Trains:");
		for (Train curTrain = _trains; curTrain != null; curTrain = curTrain.getNext()) {
			trainsSB.append(" ").append(curTrain.getName());
		}
		return trainsSB.append("\n").toString();
	}
	public String delLast(String tNameA, String tNameB) {
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);
		Train	trainB			= TrainArr[0];
		Train	trainBeforeA	= TrainArr[1];
		Train	trainA			= TrainArr[2];

		if (trainA == null) {
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainB != null) {
			return Error.getErrorStr(Error.trainExists, tNameB);
		}

		if (trainA.isSingleCar()) {
			this.New(tNameB, trainA.getFirst().getName());
			this.DeleteNext(trainBeforeA);
			return "";
		}
		String deletedName = trainA.DeleteLast();
		this.New(tNameB, deletedName);
		return "";
	}
	public String delFirst(String tNameA, String tNameB) {
		Train[]	TrainArr		= findTwoTrainsAndTrainBefore(tNameB, tNameA);
		Train	trainB			= TrainArr[0];
		Train	trainBeforeA	= TrainArr[1];
		Train	trainA			= TrainArr[2];

		if (trainA == null) {
			return Error.getErrorStr(Error.trainNotExists, tNameA);
		}
		if (trainB != null) {
			return Error.getErrorStr(Error.trainExists, tNameB);
		}

		if (trainA.isSingleCar()) {
			this.New(tNameB, trainA.getFirst().getName());
			this.DeleteNext(trainBeforeA);
			return "";
		}
		String deletedName = trainA.DeleteFirst();
		this.New(tNameB, deletedName);
		return "";
	}
}

enum Error {
	trainExists,
	trainNotExists;

	public static void printError(Error errName, String trainName) {
		switch (errName) {
			case trainExists:
				System.out.println("Train " + trainName + " already exists");
				break;
			case trainNotExists:
				System.out.println("Train " + trainName + " does not exist");
				break;
			default:
				// nothing to print
				break;
		}
	}
	public static String getErrorStr(Error errName, String trainName) {
		switch (errName) {
			case trainExists:
				return "Train ".concat(trainName).concat(" already exists\n");
			case trainNotExists:
				return "Train ".concat(trainName).concat(" does not exist\n");
			default:

		}
		// nothing to print
		return "";
	}
}

public class Source {
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int test_count = 0;
		test_count = sc.nextInt();
		// TrainStation station = new TrainStation();
		TrainList trainList = new TrainList();
		while (test_count-- > 0) {
			int				OP_count		= sc.nextInt();
			StringBuilder	output			= new StringBuilder();
			String			OP_resultStr	= "";
			while (OP_count-- > 0) {
				String OP = sc.next();

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

						OP_resultStr = trainList.insertFirst(trainName, carName);
						break;
					}
					case "InsertLast": {
						String	trainName	= sc.next();
						String	carName		= sc.next();

						OP_resultStr = trainList.insertLast(trainName, carName);
						break;
					}
					case "Display": {
						String trainName = sc.next();
						OP_resultStr = trainList.printCarList(trainName);
						break;
					}
					case "Trains": {

						OP_resultStr = trainList.getTrainsString();
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
				System.out.print(OP_resultStr);
				output.append(OP_resultStr);
			}
		}
	}
}
