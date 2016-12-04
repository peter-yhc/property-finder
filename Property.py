class Property:
    def __init__(self, href, priceRange, address, locality, postalCode, bed, bath, car, price):
        self.href = 'http://www.realestate.com.au' + href
        self.priceRange = priceRange
        self.address = address
        self.bed = bed
        self.bath = bath
        self.car = car
        self.price = price
        self.locality = locality
        self.postalCode = postalCode

    def getHref(self):
        return self.href

    def getPriceRange(self):
        return self.priceRange

    def getAddress(self):
        return self.address

    def getBed(self):
        return self.bed

    def getBath(self):
        return self.bath

    def getCar(self):
        return self.car

    def getPrice(self):
        return self.price

    def getLocality(self):
        return self.locality

    def getPostalCode(self):
        return self.postalCode
