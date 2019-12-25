package com.jogeen.barrage.common;

/**
 * 礼物的枚举类型
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public enum GiftEnum {
	BICYCLE("bicycle","自行车","感谢程序员%s送来的一辆自行车",2L),
	ELECTRIC ("electric","电瓶车","感谢老实人%s送出的一辆电瓶车",5L),
	BUS ("bus","公交车","感谢%s的公交车，太堵了",10L),
	TRUCK ("truck","货车","感谢隔壁的%s送出的一辆大货车",50L),
	CAR("car","轿车","感谢对面小卖部老板%s送出的一辆轿车，难为你了",100L),
	TRAIN("train","火车头","感谢煤老板%s送出的一辆头",250L),
	YACHT("yacht","游艇","感谢房地产富二代%s送出的一艘游艇，",500L),
	HELICOPTER("helicopter","直升机","感谢航空公司老总%s送出的一架直升机，随时准备起飞",1000L),
	FIGHTER("fighter","战斗机","感谢感谢西南地区神秘大佬%s送出的一架战斗机，我要战斗到天亮",2000L),
	ROCKET("rocket","火箭","感谢迪拜豪门%s送出的一发火箭，请带我上天吧，我要和太阳肩并肩",5000L),
	UFO("ufo","飞碟","感谢超级土豪BABA%s送出的一架飞碟，把我带走吧，已经没什么可以留念的了！！！",10000L);
	
	private String type;
	private String name;
	private String thanksMessage;
	private Long value;
	private GiftEnum(String type, String name,String thanksMessage,Long value) {
		this.type=type;
		this.thanksMessage=thanksMessage;
		this.name=name;
		this.value=value;
	}
	public String getType() {
		return type;
	}
	
	public String getThanksMessage() {
		return thanksMessage;
	}
	
	public String getName() {
		return name;
	}

	public Long getValue() {
		return value;
	}

	public static GiftEnum getGiftByType(String type) {
		for (GiftEnum gift : GiftEnum.values()) {
			if(gift.getType().equals(type)) {
				return gift;
			}
		}
		return null;
	}
	
	public static String getThanksMessageByType(String type) {
		for (GiftEnum gift : GiftEnum.values()) {
			if(gift.getType().equals(type)) {
				return gift.getThanksMessage();
			}
		}
		return "";
	}
	
	public static String getNameMessageByType(String type) {
		for (GiftEnum gift : GiftEnum.values()) {
			if(gift.getType().equals(type)) {
				return gift.getName();
			}
		}
		return "";
	}
}
