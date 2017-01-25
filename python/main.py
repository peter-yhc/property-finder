import csv

import requests
from bs4 import BeautifulSoup

from Property import Property

# ========================= Constants =========================#
baseURL = "http://www.realestate.com.au/buy/with-%numBedrooms%-bedrooms-between-%minRange%-%maxRange%-in-" \
          + "homebush%2c+nsw+2140%3b" \
          + "+homebush+west%2c+nsw+2140%3b" \
          + "+homebush+south%2c+nsw+2140%3b" \
          + "+parramatta+-+greater+region%2c+nsw%3b" \
          + "+merrylands%2c+nsw+2160%3b" \
          + "+wentworthville%2c+nsw+2145%3b" \
          + "+pennant+hills%2c+nsw+2120%3b" \
          + "+west+pennant+hills%2c+nsw+2125%3b" \
          + "+granville%2c+nsw+2142%3b" \
          + "+rhodes%2c+nsw+2138%3b" \
          + "+ryde%2c+nsw+2112%3b" \
          + "+pymble%2c+nsw+2073%3b" \
          + "+lindfield%2c+nsw+2070%3b" \
          + "+rosehill%2c+nsw+2142%3b" \
          + "+hornsby%2c+nsw+2077%3b" \
          + "+waitara%2c+nsw+2077" \
          + "/list-%page%" \
          + "?numParkingSpaces=1&numBaths=1&maxBeds=any"

result = dict()
minPrice = 250000
maxPrice = 300000
priceCeiling = 700000
numBedrooms = 2
pageNumber = 1


# ========================== Methods ==========================#

def parseSearchResults(pageSource):
    soup = BeautifulSoup(pageSource, 'html.parser')
    for article in soup.find(id='searchResultsTbl').find_all('article'):
        propertyLink = article.find('a')['href']
        if propertyLink in result:
            continue
        priceRange = str(minPrice) + '-' + str(maxPrice)
        parsePropertyPage(propertyLink, priceRange)


def parsePropertyPage(propertyLink, priceRange):
    address = locality = postalCode = ''
    bed = bath = car = price = '0'

    print("Grabbing " + propertyLink)
    propertySoup = BeautifulSoup(requests.get('http://www.realestate.com.au' + propertyLink).text, 'html.parser')

    try:
        price = propertySoup.find(id='listing_info').find('p').get_text()
        address = propertySoup.find(id='listing_header').find(itemprop='streetAddress').get_text()
        locality = propertySoup.find(id='listing_header').find(itemprop='addressLocality').get_text()
        postalCode = propertySoup.find(id='listing_header').find(itemprop='postalCode').get_text()
        bed = propertySoup.find(id='listing_info').find('dt', {"class": 'rui-icon-bed'}).findNext('dd').get_text()
        bath = propertySoup.find(id='listing_info').find('dt', {"class": 'rui-icon-bath'}).findNext('dd').get_text()
        car = propertySoup.find(id='listing_info').find('dt', {"class": 'rui-icon-car'}).findNext('dd').get_text()
    except Exception as e:
        print(e)
    result[propertyLink] = Property(propertyLink, priceRange, address, locality, postalCode, bed, bath, car, price)


# ============================ Run ============================#
try:
    baseURL = baseURL.replace('%numBedrooms%', str(numBedrooms))
    while maxPrice <= priceCeiling:
        print('Current price range ' + str(minPrice) + '-' + str(maxPrice))
        print('Current page ' + str(pageNumber))
        currentURL = baseURL.replace('%page%', str(pageNumber))
        currentURL = currentURL.replace('%minRange%', str(minPrice))
        currentURL = currentURL.replace('%maxRange%', str(maxPrice))

        response = requests.get(currentURL)

        if ('We couldn&#39;t find anything matching your search criteria' in response.text):
            minPrice += 50000
            maxPrice += 50000
            pageNumber = 1
            continue

        parseSearchResults(response.text)
        pageNumber += 1
except Exception as e:
    print(e)

csvFile = open('realestate.csv', "w")
csvWriter = csv.writer(csvFile, delimiter='|', lineterminator='\n')
csvWriter.writerow(('price range', 'price', 'address', 'locality', 'postalCode', 'beds', 'bathes', 'cars', 'link'))
for property in result.values():
    csvWriter.writerow((
        property.getPriceRange(),
        property.getPrice(),
        property.getAddress(),
        property.getLocality(),
        property.getPostalCode(),
        property.getBed(),
        property.getBath(),
        property.getCar(),
        property.getHref()
    ))

csvFile.close()
print("End of job")
