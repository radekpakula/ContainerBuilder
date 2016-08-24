package pl.containerbuilder.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ObjectGenerator {

	public static List<ExampleUser> generateUsers(int count){
		Random rand = new Random();
		List<ExampleUser> examples = new ArrayList<ExampleUser>();
		for(int i=0;i<count;i++){
			ExampleUser u = new ExampleUser();
			u.setId(new Long(i));
			u.setAge(rand.nextInt(50));
			u.setGender(rand.nextBoolean());
			u.setUserType(UserTypeEnum.values()[rand.nextInt(3)]);
			u.setName(names[rand.nextInt(names.length)]);
			if(rand.nextBoolean()){
				u.setExampleGroup(new ExampleGroup());
				u.getExampleGroup().setActive(rand.nextBoolean());
				u.getExampleGroup().setName(group[rand.nextInt(group.length)]);
			}
			Long l = new Long((new Random().nextLong()+"").substring(0, 8));
			l=l<0 ? l=l*-1 : l;
			Long time = new Long("14720"+l);
			u.setDate(new Date(time<0 ? time*-1 : time));
			examples.add(u);
		}
		return examples;
	}
	public static String[] names = new String[]{
		"TOM","MARK","ANN","PARK","NEWTON","FOO","BAR","ANDREE","MAX","JOANN","SID","DUCK"	
	};
	public static String[] group = new String[]{
			"DEV","ADMIN","CALLER","DESIGNER"	
	};
}
